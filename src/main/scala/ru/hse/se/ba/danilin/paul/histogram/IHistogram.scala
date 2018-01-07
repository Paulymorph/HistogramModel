package ru.hse.se.ba.danilin.paul.histogram

trait IHistogram[E] {
  def apply(element: E): Int
  def elementsUniverse: ElementsUniverse[E]
  def elementsPresent: Set[E]
  def subHistogram(elements: ElementsUniverse[E]): IHistogram[E]
}