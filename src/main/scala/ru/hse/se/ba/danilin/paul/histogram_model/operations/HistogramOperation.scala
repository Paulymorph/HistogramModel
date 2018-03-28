package ru.hse.se.ba.danilin.paul.histogram_model.operations

import ru.hse.se.ba.danilin.paul.histogram_model.histogram.{ElementsUniverse, Histogram, HistogramImpl}
import ru.hse.se.ba.danilin.paul.histogram_model.histogram.Implicits.SetUniverse

sealed trait Operation {
  def narity: Double
}

sealed trait HistogramOperation extends Operation

abstract class HistogramUnaryOperation extends HistogramOperation {
  def apply[E](histogram: Histogram[E]): Histogram[E]

  override val narity: Double = 1
}

abstract class HistogramBinaryOperation extends HistogramOperation {
  def apply[E](first: Histogram[E], second: Histogram[E]): Histogram[E]

  def apply[E](histogram: Histogram[E], properties: ElementsUniverse[E]): Histogram[E] =
    this.apply(histogram, histogram.subHistogram(properties))

  override val narity: Double = 2
}

abstract class HistogramBinaryMergeOperation extends HistogramBinaryOperation {
  override def apply[E](first: Histogram[E], second: Histogram[E]): Histogram[E] = {
    val allElements = first.elementsPresent ++ second.elementsPresent

    val histPairs = for {
      element <- allElements
      leftCount = first(element)
      rightCount = second(element)
      mergeRes = merge(leftCount, rightCount) if mergeRes != 0
    } yield element -> mergeRes

    HistogramImpl(histPairs.toMap)(new SetUniverse(allElements))
  }

  /**
    * Used to merge a histogram element of 2 histograms
    * @param leftCount the count of an element from the left histogram
    * @param rightCount the count of an element from the right histogram
    * @return the result count of the element if the resulting histogram
    */
  def merge(leftCount: Double, rightCount: Double): Double
}

abstract class AggregateOperation extends Operation {
  def apply[E](left: Histogram[E], right: Histogram[E]): Double

  override val narity: Double = 2
}
