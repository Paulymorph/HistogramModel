package ru.hse.se.ba.danilin.paul.histogram_model.histogram

import ru.hse.se.ba.danilin.paul.histogram_model.operations._

/**
  * Basic trait for histogram and its operations
  * @tparam E The element of the histogram
  */
trait Histogram[E] {
  /**
    * The presence of the element
    * @param element The element of a histogram
    * @return The presence of the element
    */
  def apply(element: E): Double

  /**
    * The elements universe of the histogram
    * @return The elements universe of the histogram
    */
  def elementsUniverse: ElementsUniverse[E]

  /**
    * The set of elements present in the histogram
    * @return The set of elements present in the histogram
    */
  def elementsPresent: Set[E]

  /**
    * A subhistogram of the histogram
    * @param elements A new space of the histogram
    * @return A new subhistogram of the histogram
    */
  def subHistogram(elements: ElementsUniverse[E]): Histogram[E]

  /**
    * Intersects the histogram with another one
    * @param other The histogram to intersect with
    * @return The intersection with the histogram
    */
  def intersect(other: Histogram[E]): Histogram[E] = Intersect(this, other)

  /**
    * Intersects the histogram with its subhistogram
    * @param other The subhistogram space
    * @return The intersection with the subhistogram
    */
  def intersect(other: ElementsUniverse[E]): Histogram[E] = Intersect(this, other)

  /**
    * The subtraction of the histogram out of the current
    * @param other The histogram to subtract
    * @return The result of the subtraction of the other histogram
    */
  def subtract(other: Histogram[E]): Histogram[E] = Subtract(this, other)

  /**
    * The subtraction of the subhistogram out of the current
    * @param other The subhistogram to subtract
    * @return The result of the subtraction of the subhistogram
    */
  def subtract(other: ElementsUniverse[E]): Histogram[E] = Subtract(this, other)

  /**
    * The subtraction of the histogram out of the current
    * @param other The histogram to subtract
    * @return The result of the subtraction of the other histogram
    */
  def -(other: Histogram[E]): Histogram[E] = subtract(other)

  /**
    * The subtraction of the subhistogram out of the current
    * @param other The subhistogram to subtract
    * @return The result of the subtraction of the subhistogram
    */
  def -(other: ElementsUniverse[E]): Histogram[E] = subtract(other)

  /**
    * The unity of the histogram with another histogram
    * @param other The histogram to unite with
    * @return The unity of the histogram with another histogram
    */
  def unite(other: Histogram[E]): Histogram[E] = Unite(this, other)

  /**
    * The unity of the histogram with its subhistogram
    * @param other The subhistogram to unite with
    * @return The unity of the histogram with its subhistogram
    */
  def unite(other: ElementsUniverse[E]): Histogram[E] = Unite(this, other)

  /**
    * The unity of the histogram with another histogram
    * @param other The histogram to unite with
    * @return The unity of the histogram with another histogram
    */
  def +(other: Histogram[E]): Histogram[E] = unite(other)

  /**
    * The unity of the histogram with its subhistogram
    * @param other The subhistogram to unite with
    * @return The unity of the histogram with its subhistogram
    */
  def +(other: ElementsUniverse[E]): Histogram[E] = unite(other)

  /**
    * The AND operation of the histogram and another one
    * @param other The other histogram to execute operation on
    * @return The result of the AND operation
    */
  def and(other: Histogram[E]): Histogram[E] = And(this, other)

  /**
    * The AND operation of the histogram and its subhistogram
    * @param other The subhistogram to execute operation on
    * @return The result of the AND operation
    */
  def and(other: ElementsUniverse[E]): Histogram[E] = And(this, other)

  /**
    * The AND operation of the histogram and another one
    * @param other The other histogram to execute operation on
    * @return The result of the AND operation
    */
  def &(other: Histogram[E]): Histogram[E] = and(other)

  /**
    * The AND operation of the histogram and its subhistogram
    * @param other The subhistogram to execute operation on
    * @return The result of the AND operation
    */
  def &(other: ElementsUniverse[E]): Histogram[E] = and(other)

  /**
    * The BESIDES operation of the histogram and another one
    * @param other The other histogram to execute operation on
    * @return The result of the BESIDES operation
    */
  def besides(other: Histogram[E]): Histogram[E] = Besides(this, other)

  /**
    * The BESIDES operation of the histogram and its subhistogram
    * @param other The subhistogram to execute operation on
    * @return The result of the BESIDES operation
    */
  def besides(other: ElementsUniverse[E]): Histogram[E] = Besides(this, other)

  /**
    * The NOT operation over the histogram
    * @return The NOT operation over the histogram
    */
  def not = Not(this)

  /**
    * The OR operation of the histogram and another one
    * @param other The other histogram to execute operation on
    * @return The result of the OR operation
    */
  def or(other: Histogram[E]): Histogram[E] = Or(this, other)

  /**
    * The OR operation of the histogram and its subhistogram
    * @param other The subhistogram to execute operation on
    * @return The result of the OR operation
    */
  def or(other: ElementsUniverse[E]): Histogram[E] = Or(this, other)

  /**
    * The OR operation of the histogram and another one
    * @param other The other histogram to execute operation on
    * @return The result of the OR operation
    */
  def |(other: Histogram[E]): Histogram[E] = or(other)

  /**
    * The OR operation of the histogram and its subhistogram
    * @param other The subhistogram to execute operation on
    * @return The result of the OR operation
    */
  def |(other: ElementsUniverse[E]): Histogram[E] = or(other)

  /**
    * The XOR operation of the histogram and another one
    * @param other The other histogram to execute operation on
    * @return The result of the XOR operation
    */
  def xor(other: Histogram[E]): Histogram[E] = Xor(this, other)

  /**
    * The XOR operation of the histogram and its subhistogram
    * @param other The subhistogram to execute operation on
    * @return The result of the XOR operation
    */
  def xor(other: ElementsUniverse[E]): Histogram[E] = Xor(this, other)

  /**
    * The similarity of the histograms
    * @param other The other histogram to measure similarity on
    * @return The similarity of the histograms
    */
  def similar(other: Histogram[E]) = Similar(this, other)

  /**
    * The XBESIDES operation of the histogram and another one
    * @param other The other histogram to execute operation on
    * @return The result of the XBESIDES operation
    */
  def xbesides(other: Histogram[E]): Histogram[E] = XBesides(this, other)

  /**
    * The XBESIDES operation of the histogram and its subhistogram
    * @param other The subhistogram to execute operation on
    * @return The result of the XBESIDES operation
    */
  def xbesides(other: ElementsUniverse[E]): Histogram[E] = XBesides(this, other)

  /**
    * The normalized histogram
    * @return The normalized histogram
    */
  def normalize(): Histogram[E]
}