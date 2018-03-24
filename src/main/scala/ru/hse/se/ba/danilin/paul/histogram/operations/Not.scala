package ru.hse.se.ba.danilin.paul.histogram.operations
import ru.hse.se.ba.danilin.paul.histogram.{IHistogram, OneHistogram}

object Not extends HistogramUnaryOperation {
  override def apply[E](histogram: IHistogram[E]): IHistogram[E] =
    new OneHistogram[E]()(histogram.elementsUniverse) besides histogram
}
