package ru.hse.se.ba.danilin.paul.histogram_model.histogram

import org.json4s.{CustomSerializer, Formats, JDouble, JField, JObject}
import ru.hse.se.ba.danilin.paul.histogram_model.atomizers._

object Implicits {
  implicit class toHistogramClass[S, O](source: S)(implicit atomizer: Atomizer[S, O]) {
    def toHistogram = HistogramImpl.extract(source)(atomizer)
  }

  implicit class SetUniverse[E](unverseSet: Set[E]) extends ElementsUniverse[E] {
    override def isElementInUniverse(element: E): Boolean = unverseSet.contains(element)
  }

  implicit val stringToWords: StringAtomizer[String] = new StringToWords

  implicit val imageToPixels: ImageAtomizer[Color] = new ImageToPixels

  implicit val formats: Formats = org.json4s.DefaultFormats + new StringHistSerializer + new PixelHistSerializer

  class StringHistSerializer extends CustomSerializer[HistogramImpl[String]](format => (
    {
      case JObject(JField("histogram", JObject(s)) :: Nil) =>
        val hist = s.map { case (word, JDouble(count)) =>
          word -> count
        }.toMap
        HistogramImpl(hist)
    },
    {
      case HistogramImpl(hist) if hist.keys.headOption.exists(_.isInstanceOf[String]) =>
        val histogramTransformed = hist.asInstanceOf[Map[String, Double]]
          .mapValues(JDouble(_))
        JObject(JField("histogram", new JObject(histogramTransformed.toList)) :: Nil)
    }
  ))

  class PixelHistSerializer extends CustomSerializer[HistogramImpl[Color]](format => (
    {
      case JObject(JField("histogram", JObject(s)) :: Nil) =>
        val hist = s.map{ case (colorString, JDouble(count)) =>
          val color = Color.allColors.find(_.toString == colorString).getOrElse(Red)
          color -> count
        }.toMap
        HistogramImpl(hist)
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
