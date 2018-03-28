package ru.hse.se.ba.danilin.paul.histogram_model.operations

import ru.hse.se.ba.danilin.paul.histogram_model.histogram.Histogram

object And extends HistogramBinaryOperation {
  def count[E](histogram: Histogram[E]) =
    histogram.elementsPresent.map(histogram(_)).sum

  override def apply[E](first: Histogram[E], second: Histogram[E]): Histogram[E] =
    if (count(first) < count(second))
      first
    else
      second
}
