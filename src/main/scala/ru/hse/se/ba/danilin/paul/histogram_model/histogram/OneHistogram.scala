package ru.hse.se.ba.danilin.paul.histogram_model.histogram

/**
  * The full histogram
  * @param universe The universe of the histogram
  * @tparam E The element of the histogram
  */
class OneHistogram[E](implicit universe: ElementsUniverse[E]) extends Histogram[E] {
  /**
    * The full presence of the element
    * @param element The element of a histogram
    * @return The presence of the element
    */
  override def apply(element: E): Double = Double.MaxValue

  /**
    * The elements universe of the histogram
    * @return The elements universe of the histogram
    */
  override def elementsUniverse: ElementsUniverse[E] = universe

  /**
    * The elements present in the histogram
    */
  override val elementsPresent: Set[E] = Set.empty[E]

  /**
    * Subhistogram
    * @param elements A new space of the histogram
    * @return A new subhistogram of the histogram
    */
  override def subHistogram(elements: ElementsUniverse[E]): Histogram[E] = new OneHistogram()(elements)

  /**
    * Normalized histogram
    * @return The normalized histogram
    */
  override def normalize(): Histogram[E] = this
}
