package ru.hse.se.ba.danilin.paul.histogram_model.histogram

import org.json4s.{CustomSerializer, Formats, JDouble, JField, JObject}
import ru.hse.se.ba.danilin.paul.histogram_model.atomizers._

import scala.collection.mutable

/**
  * The object with implicits for the project
  */
object Implicits {

  /**
    * Class to add `toHistogram` method to any class
    * @param source The source to extract histogram from
    * @param atomizer The atomizer for histogram construction
    * @tparam S The type of the source
    * @tparam O The type of the elements
    */
  implicit class ToHistogramClass[S, O](source: S)(implicit atomizer: Atomizer[S, O]) {
    /**
      * Extracts histogram from the object
      * @return The histogram extracted from this
      */
    def toHistogram = HistogramImpl.extract(source)(atomizer)
  }

  /**
    * An implicit class for construction universe from a set of elements
    * @param unverseSet The set of elements for the universe
    * @tparam E The element type
    */
  implicit class SetUniverse[E](unverseSet: Set[E]) extends ElementsUniverse[E] {
    /**
      * If the element in the set universe
      * @param element The element to check
      * @return True if the element is in universe, false otherwise
      */
    override def isElementInUniverse(element: E): Boolean = unverseSet.contains(element)
  }

  /**
    * String => words atomizer
    */
  implicit val stringToWords: StringAtomizer[String] = new StringToWords

  /**
    * Image => colors atomizer
    */
  implicit val imageToPixelColors: ImageAtomizer[Color] = new ImageToPixels

  /**
    * Serialization formats
    */
  implicit val formats: Formats = org.json4s.DefaultFormats + new StringHistSerializer + new PixelHistSerializer

  /**
    * String histogram serializer
    */
  class StringHistSerializer extends CustomSerializer[HistogramImpl[String]](format => (
    {
      case JObject(JField("histogram", JObject(s)) :: Nil) =>
        val hist = s.map { case (word, JDouble(count)) =>
          word -> count
        }.toMap
        HistogramImpl(mutable.Map(hist.toSeq: _*))
    },
    {
      case HistogramImpl(hist) if hist.keys.headOption.exists(_.isInstanceOf[String]) =>
        val histogramTransformed = hist.asInstanceOf[Map[String, Double]]
          .mapValues(JDouble(_))
        JObject(JField("histogram", new JObject(histogramTransformed.toList)) :: Nil)
    }
  ))

  /**
    * Color histogram serializer
    */
  class PixelHistSerializer extends CustomSerializer[HistogramImpl[Color]](format => (
    {
      case JObject(JField("histogram", JObject(s)) :: Nil) =>
        val hist = s.map{ case (colorString, JDouble(count)) =>
          val color = Color.allColors.find(_.toString == colorString).getOrElse(Red)
          color -> count
        }.toMap
        HistogramImpl(mutable.Map(hist.toSeq: _*))
    },
    {
      case HistogramImpl(hist) if hist.keys.headOption.exists(_.isInstanceOf[Color]) =>
        val histogramTransformed = hist.asInstanceOf[Map[Color, Double]]
          .map{ case (pixel, count) =>
            pixel.toString -> JDouble(count)}
        JObject(JField("histogram", new JObject(histogramTransformed.toList)) :: Nil)
    }
  ))
}
