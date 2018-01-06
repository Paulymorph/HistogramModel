package ru.hse.se.ba.danilin.paul.histogram.operations

import ru.hse.se.ba.danilin.paul.histogram.IHistogram

sealed trait HistogramOperation

abstract class HistogramUnaryOperation extends HistogramOperation {
  def apply[E](histogram: IHistogram[E]): IHistogram[E]
}

abstract class HistogramBinaryOperation extends HistogramOperation {
  def apply[E](first: IHistogram[E], second: IHistogram[E]): IHistogram[E]

  def apply[E](histogram: IHistogram[E], properties: Set[E]): IHistogram[E] =
    this(histogram, histogram.subHistogram(properties))
}
