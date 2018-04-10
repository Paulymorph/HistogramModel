package ru.hse.se.ba.danilin.paul.histogram_model.histogram

class OneHistogram[E](implicit universe: ElementsUniverse[E]) extends Histogram[E] {
  override def apply(element: E): Double = Double.MaxValue

  override def elementsUniverse: ElementsUniverse[E] = universe

  override val elementsPresent: Set[E] = Set.empty[E]

  override def subHistogram(elements: ElementsUniverse[E]): Histogram[E] = new OneHistogram()(elements)

  override def normalize(): Histogram[E] = this
}
