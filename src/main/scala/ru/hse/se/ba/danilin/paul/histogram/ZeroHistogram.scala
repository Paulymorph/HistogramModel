package ru.hse.se.ba.danilin.paul.histogram

class ZeroHistogram[E](implicit universe: ElementsUniverse[E]) extends IHistogram[E] {
  override def apply(element: E): Double = 0

  override def elementsUniverse: ElementsUniverse[E] = universe

  override val elementsPresent: Set[E] = Set.empty[E]

  override def subHistogram(elements: ElementsUniverse[E]): IHistogram[E] = this
}
