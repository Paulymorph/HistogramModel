package ru.hse.se.ba.danilin.paul.histogram.operations
import ru.hse.se.ba.danilin.paul.histogram.{Histogram, OneHistogram}

object Not extends HistogramUnaryOperation {
  override def apply[E](histogram: Histogram[E]): Histogram[E] =
    new OneHistogram[E]()(histogram.elementsUniverse) besides histogram
}
