package ru.hse.se.ba.danilin.paul.histogram.operations

import ru.hse.se.ba.danilin.paul.histogram.Implicits.SetUniverse
import ru.hse.se.ba.danilin.paul.histogram.{ElementsUniverse, Histogram, IHistogram}

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
    this.apply(histogram, histogram.subHistogram(properties))

  override val narity: Double = 2
}

abstract class HistogramBinaryMergeOperation extends HistogramBinaryOperation {
  override def apply[E](first: IHistogram[E], second: IHistogram[E]): IHistogram[E] = {
    val allElements = first.elementsPresent ++ second.elementsPresent

    val histPairs = for {
      element <- allElements
      leftCount = first(element)
      rightCount = second(element)
      mergeRes = merge(leftCount, rightCount) if mergeRes != 0
    } yield element -> mergeRes

    Histogram(histPairs.toMap)(new SetUniverse(allElements))
  }

  /**
    * Used to merge a histogram element of 2 histograms
    * @param leftCount the count of an element from the left histogram
    * @param rightCount the count of an element from the right histogram
    * @return the result count of the element if the resulting histogram
    */
  protected def merge(leftCount: Double, rightCount: Double): Double
}

abstract class AggregateOperation extends Operation {
  def apply[E](left: IHistogram[E], right: IHistogram[E]): Double

  override val narity: Double = 2
}
