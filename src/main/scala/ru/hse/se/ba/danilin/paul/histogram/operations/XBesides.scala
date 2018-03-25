package ru.hse.se.ba.danilin.paul.histogram.operations
import ru.hse.se.ba.danilin.paul.histogram.{Histogram, ZeroHistogram}

object XBesides extends HistogramBinaryOperation {
  override def apply[E](first: Histogram[E], second: Histogram[E]): Histogram[E] =
    if (And.count(second) > 0)
      new ZeroHistogram()(first.elementsUniverse)
    else
      first
}
