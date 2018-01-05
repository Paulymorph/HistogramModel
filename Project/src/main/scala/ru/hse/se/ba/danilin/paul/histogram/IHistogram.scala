package ru.hse.se.ba.danilin.paul.histogram

trait IHistogram[E] {
  def apply(element: E): Int
  def elementsUniverse: Set[E]
  def elementsPresent: Set[E]
  def subHistogram(elements: Set[E]): IHistogram[E]
}