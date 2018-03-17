package ru.hse.se.ba.danilin.paul.histogram.operations
import ru.hse.se.ba.danilin.paul.histogram.IHistogram

object And extends HistogramBinaryOperation {
  def count[E](histogram: IHistogram[E]) =
    histogram.elementsPresent.map(histogram(_)).sum

  override def apply[E](first: IHistogram[E], second: IHistogram[E]): IHistogram[E] =
    if (count(first) < count(second))
      first
    else
      second
}
