package ru.hse.se.ba.danilin.paul.histogram.operations

import ru.hse.se.ba.danilin.paul.histogram.{ElementsUniverse, IHistogram}

sealed trait Operation {
  def narity: Double
}

sealed trait HistogramOperation extends Operation

abstract class HistogramUnaryOperation extends HistogramOperation {
  def apply[E](histogram: IHistogram[E]): IHistogram[E]

  override val narity: Double = 1
}

abstract class HistogramBinaryOperation extends HistogramOperation {
  def apply[E](first: IHistogram[E], second: IHistogram[E]): IHistogram[E]

  def apply[E](histogram: IHistogram[E], properties: ElementsUniverse[E]): IHistogram[E] =
    this (histogram, histogram.subHistogram(properties))

  override val narity: Double = 2
}

abstract class AggregateOperation extends Operation {
  def apply[E](histogram: IHistogram[E]): Double

  override val narity: Double = 1
}
