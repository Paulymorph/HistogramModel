package ru.hse.se.ba.danilin.paul.histogram

class OneHistogram[E](implicit universe: ElementsUniverse[E]) extends IHistogram[E] {
  override def apply(element: E): Double = Double.MaxValue

  override def elementsUniverse: ElementsUniverse[E] = universe

  override val elementsPresent: Set[E] = Set.empty[E]

  override def subHistogram(elements: ElementsUniverse[E]): IHistogram[E] = this
}