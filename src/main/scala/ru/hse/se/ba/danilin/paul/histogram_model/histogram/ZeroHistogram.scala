package ru.hse.se.ba.danilin.paul.histogram_model.histogram

/**
  * The zero histogram
  * @param universe The universe of the histogram
  * @tparam E The element of the histogram
  */
class ZeroHistogram[E](implicit universe: ElementsUniverse[E]) extends Histogram[E] {
  /**
    * No elements in the histogram
    * @param element The element of a histogram
    * @return The presence (absence) of the element
    */
  override def apply(element: E): Double = 0

  /**
    * The universe of the elements
    * @return The elements universe of the histogram
    */
  override def elementsUniverse: ElementsUniverse[E] = universe

  /**
    * No elements present
    */
  override val elementsPresent: Set[E] = Set.empty[E]

  /**
    * A subhistogram of the histogram
    * @param elements A new space of the histogram
    * @return A new subhistogram of the histogram
    */
  override def subHistogram(elements: ElementsUniverse[E]): Histogram[E] = this

  /**
    * the normalized histogrm
    * @return The normalized histogram
    */
  override def normalize(): Histogram[E] = this
}
