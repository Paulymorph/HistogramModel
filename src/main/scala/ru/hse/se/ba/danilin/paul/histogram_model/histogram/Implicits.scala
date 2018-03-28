package ru.hse.se.ba.danilin.paul.histogram_model.histogram

import ru.hse.se.ba.danilin.paul.histogram_model.atomizers._

object Implicits {
  implicit class toHistogramClass[S, O](source: S)(implicit atomizer: Atomizer[S, O]) {
    def toHistogram = HistogramImpl.extract(source)(atomizer)
  }

  implicit class SetUniverse[E](unverseSet: Set[E]) extends ElementsUniverse[E] {
    override def isElementInUniverse(element: E): Boolean = unverseSet.contains(element)
  }

  implicit val stringToWords: StringAtomizer[String] = new StringToWords

  implicit val imageToPixels: ImageAtomizer[Pixel] = new ImageToPixels
}
