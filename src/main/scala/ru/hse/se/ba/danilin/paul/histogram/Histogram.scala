package ru.hse.se.ba.danilin.paul.histogram

import ru.hse.se.ba.danilin.paul.histogram.operations._

trait Histogram[E] {
  def apply(element: E): Double
  def elementsUniverse: ElementsUniverse[E]
  def elementsPresent: Set[E]
  def subHistogram(elements: ElementsUniverse[E]): Histogram[E]

  def intersect(other: Histogram[E]): Histogram[E] = Intersect(this, other)
  def intersect(other: ElementsUniverse[E]): Histogram[E] = Intersect(this, other)

  def subtract(other: Histogram[E]): Histogram[E] = Subtract(this, other)
  def subtract(other: ElementsUniverse[E]): Histogram[E] = Subtract(this, other)
  def -(other: Histogram[E]): Histogram[E] = subtract(other)
  def -(other: ElementsUniverse[E]): Histogram[E] = subtract(other)

  def unite(other: Histogram[E]): Histogram[E] = Unite(this, other)
  def unite(other: ElementsUniverse[E]): Histogram[E] = Unite(this, other)
  def +(other: Histogram[E]): Histogram[E] = unite(other)
  def +(other: ElementsUniverse[E]): Histogram[E] = unite(other)

  def and(other: Histogram[E]): Histogram[E] = And(this, other)
  def and(other: ElementsUniverse[E]): Histogram[E] = And(this, other)
  def &(other: Histogram[E]): Histogram[E] = and(other)
  def &(other: ElementsUniverse[E]): Histogram[E] = and(other)

  def besides(other: Histogram[E]): Histogram[E] = Besides(this, other)
  def besides(other: ElementsUniverse[E]): Histogram[E] = Besides(this, other)

  def not = Not(this)

  def or(other: Histogram[E]): Histogram[E] = Or(this, other)
  def or(other: ElementsUniverse[E]): Histogram[E] = Or(this, other)
  def |(other: Histogram[E]): Histogram[E] = or(other)
  def |(other: ElementsUniverse[E]): Histogram[E] = or(other)

  def xor(other: Histogram[E]): Histogram[E] = Xor(this, other)
  def xor(other: ElementsUniverse[E]): Histogram[E] = Xor(this, other)

  def similar(other: Histogram[E]) = Similar(this, other)

  def xbesides(other: Histogram[E]): Histogram[E] = XBesides(this, other)
  def xbesides(other: ElementsUniverse[E]): Histogram[E] = XBesides(this, other)
}