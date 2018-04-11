package ru.hse.se.ba.danilin.paul.histogram_model.operations
import ru.hse.se.ba.danilin.paul.histogram_model.histogram.{Histogram, ZeroHistogram}

/**
  * The XBESIDES operation
  */
object XBesides extends HistogramBinaryOperation {
  override def apply[E](first: Histogram[E], second: Histogram[E]): Histogram[E] =
    if (And.count(second) > 0)
      new ZeroHistogram()(first.elementsUniverse)
    else
      first
}
