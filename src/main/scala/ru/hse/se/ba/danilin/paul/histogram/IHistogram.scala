package ru.hse.se.ba.danilin.paul.histogram

import ru.hse.se.ba.danilin.paul.histogram.operations.{Intersect, Subtract, Unite}

trait IHistogram[E] {
  def apply(element: E): Double
  def elementsUniverse: ElementsUniverse[E]
  def elementsPresent: Set[E]
  def subHistogram(elements: ElementsUniverse[E]): IHistogram[E]

  def intersect(other: IHistogram[E]): IHistogram[E] = Intersect(this, other)
  def intersect(other: ElementsUniverse[E]): IHistogram[E] = Intersect(this, other)

  def subtract(other: IHistogram[E]): IHistogram[E] = Subtract(this, other)
  def subtract(other: ElementsUniverse[E]): IHistogram[E] = Subtract(this, other)
  def -(other: IHistogram[E]): IHistogram[E] = subtract(other)
  def -(other: ElementsUniverse[E]): IHistogram[E] = subtract(other)

  def unite(other: IHistogram[E]): IHistogram[E] = Unite(this, other)
  def unite(other: ElementsUniverse[E]): IHistogram[E] = Unite(this, other)
  def +(other: IHistogram[E]): IHistogram[E] = unite(other)
  def +(other: ElementsUniverse[E]): IHistogram[E] = unite(other)
}