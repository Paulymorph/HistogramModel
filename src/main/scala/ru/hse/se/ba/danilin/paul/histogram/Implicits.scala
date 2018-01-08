package ru.hse.se.ba.danilin.paul.histogram

import ru.hse.se.ba.danilin.paul.histogram.atomizers.{IAtomizer, IStringAtomizer, StringToWords}

object Implicits {
  implicit class toHistogramClass[S, O](source: S)(implicit atomizer: IAtomizer[S, O]) {
    def toHistogram = Histogram.extract(source)(atomizer)
  }

  implicit class SetUniverse[E](unverseSet: Set[E]) extends ElementsUniverse[E] {
    override def isElementInUniverse(element: E): Boolean = unverseSet.contains(element)
  }

  implicit val stringToWords: IStringAtomizer[String] = new StringToWords
}
