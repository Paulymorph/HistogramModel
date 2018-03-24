package ru.hse.se.ba.danilin.paul.histogram

import ru.hse.se.ba.danilin.paul.histogram.operations._

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

  def and(other: IHistogram[E]): IHistogram[E] = And(this, other)
  def and(other: ElementsUniverse[E]): IHistogram[E] = And(this, other)
  def &(other: IHistogram[E]): IHistogram[E] = and(other)
  def &(other: ElementsUniverse[E]): IHistogram[E] = and(other)

  def besides(other: IHistogram[E]): IHistogram[E] = Besides(this, other)
  def besides(other: ElementsUniverse[E]): IHistogram[E] = Besides(this, other)

  def not = Not(this)

  def or(other: IHistogram[E]): IHistogram[E] = Or(this, other)
  def or(other: ElementsUniverse[E]): IHistogram[E] = Or(this, other)
  def |(other: IHistogram[E]): IHistogram[E] = or(other)
  def |(other: ElementsUniverse[E]): IHistogram[E] = or(other)

  def xor(other: IHistogram[E]): IHistogram[E] = Xor(this, other)
  def xor(other: ElementsUniverse[E]): IHistogram[E] = Xor(this, other)

  def similar(other: IHistogram[E]) = Similar(this, other)

  def xbesides(other: IHistogram[E]): IHistogram[E] = XBesides(this, other)
  def xbesides(other: ElementsUniverse[E]): IHistogram[E] = XBesides(this, other)
}