package ru.hse.se.ba.danilin.paul.histogram_model.operations

import ru.hse.se.ba.danilin.paul.histogram_model.histogram.Histogram

/**
  * The AND operation
  */
object And extends HistogramBinaryOperation {
  /**
    * The `value` of the histogram
    * @param histogram The histogram to find value of
    * @tparam E the type of the elements
    * @return The sum of the elements of the histogram
    */
  def count[E](histogram: Histogram[E]) =
    histogram.elementsPresent.map(histogram(_)).sum

  /**
    * Applies the AND function
    * @param first The left operand to apply the function to
    * @param second The right operand to apply the function to
    * @tparam E The element type of the histogram
    * @return The result of the function
    */
  override def apply[E](first: Histogram[E], second: Histogram[E]): Histogram[E] =
    if (count(first) < count(second))
      first
    else
      second
}
