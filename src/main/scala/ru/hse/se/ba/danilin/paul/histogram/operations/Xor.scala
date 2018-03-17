package ru.hse.se.ba.danilin.paul.histogram.operations
import ru.hse.se.ba.danilin.paul.histogram.IHistogram

object Xor extends HistogramBinaryOperation {
  private def count[E](histogram: IHistogram[E]) =
    And.count(histogram)

  override def apply[E](first: IHistogram[E], second: IHistogram[E]): IHistogram[E] =
    if (count(first) > count(second))
      first
    else
      second
}
