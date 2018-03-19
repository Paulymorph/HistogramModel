package ru.hse.se.ba.danilin.paul.histogram.operations
import ru.hse.se.ba.danilin.paul.histogram.{IHistogram, ZeroHistogram}

object XBesides extends HistogramBinaryOperation {
  override def apply[E](first: IHistogram[E], second: IHistogram[E]): IHistogram[E] =
    if (And.count(second) > 0)
      new ZeroHistogram()(first.elementsUniverse)
    else
      first
}
