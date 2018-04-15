package ru.hse.se.ba.danilin.paul.histogram_model.operations

import ru.hse.se.ba.danilin.paul.histogram_model.histogram.Implicits.SetUniverse
import ru.hse.se.ba.danilin.paul.histogram_model.histogram.{ElementsUniverse, Histogram, HistogramImpl}

import scala.collection.mutable

/**
  * The interface of the operations over histograms
  */
sealed trait Operation extends Serializable {
  /**
    * The narity of the operation
    * @return
    */
  def narity: Int
}

/**
  * The operations with a histogram as a result
  */
sealed trait HistogramOperation extends Operation

/**
  * Unary histogram operation
  */
abstract class HistogramUnaryOperation extends HistogramOperation {
  /**
    * Applies the function to the histogram
    * @param histogram The histogram to apply the function to
    * @tparam E The element type of the histogram
    * @return The result of the function
    */
  def apply[E](histogram: Histogram[E]): Histogram[E]

  override val narity: Int = 1
}

/**
  * Binary histogram operation
  */
abstract class HistogramBinaryOperation extends HistogramOperation {
  /**
    * Applies the function to the operands
    * @param first The left operand to apply the function to
    * @param second The right operand to apply the function to
    * @tparam E The element type of the histogram
    * @return The result of the function
    */
  def apply[E](first: Histogram[E], second: Histogram[E]): Histogram[E]

  /**
    * Applies the function to the histogram and its subhistogram
    * @param histogram The histogram to apply operation to
    * @param properties The subhistogram to apply operation to
    * @tparam E The type of the elements
    * @return The result of the function
    */
  def apply[E](histogram: Histogram[E], properties: ElementsUniverse[E]): Histogram[E] =
    this.apply(histogram, histogram.subHistogram(properties))

  override val narity: Int = 2
}

/**
  * A binary operation with by element operation applying
  */
abstract class HistogramBinaryMergeOperation extends HistogramBinaryOperation {
  /**
    * Applies the function to the operands
    * @param first The left operand to apply the function to
    * @param second The right operand to apply the function to
    * @tparam E The element type of the histogram
    * @return The result of the function
    */
  override def apply[E](first: Histogram[E], second: Histogram[E]): Histogram[E] = {
    val allElements = first.elementsPresent ++ second.elementsPresent

    val histPairs = for {
      element <- allElements
      leftCount = first(element)
      rightCount = second(element)
      mergeRes = merge(leftCount, rightCount) if mergeRes != 0
    } yield element -> mergeRes

    HistogramImpl(mutable.Map(histPairs.toMap.toSeq: _*))(new SetUniverse(allElements))
  }

  /**
    * Used to merge a histogram element of 2 histograms
    * @param leftCount The count of an element from the left histogram
    * @param rightCount The count of an element from the right histogram
    * @return The result count of the element in the resulting histogram
    */
  def merge(leftCount: Double, rightCount: Double): Double
}

/**
  * A binary operation that returns Double
  */
abstract class AggregateOperation extends Operation {
  /**
    * Applies the function to the operands
    * @param left The left histogram operand
    * @param right The right histogram operand
    * @tparam E The type of the elements of the histograms
    * @return The result of the function
    */
  def apply[E](left: Histogram[E], right: Histogram[E]): Double

  override val narity: Int = 2
}
