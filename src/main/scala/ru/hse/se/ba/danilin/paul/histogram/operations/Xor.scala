package ru.hse.se.ba.danilin.paul.histogram.operations
import ru.hse.se.ba.danilin.paul.histogram.Histogram

object Xor extends HistogramBinaryOperation {
  private def count[E](histogram: Histogram[E]) =
    And.count(histogram)

  override def apply[E](first: Histogram[E], second: Histogram[E]): Histogram[E] =
    if (count(first) > count(second))
      first
    else
      second
}
