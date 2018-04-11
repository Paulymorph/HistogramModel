package ru.hse.se.ba.danilin.paul.histogram_model.operations

import ru.hse.se.ba.danilin.paul.histogram_model.histogram.Histogram

/**
  * The SIMILARITY operation
  */
object Similar extends AggregateOperation {
  override def apply[E](left: Histogram[E], right: Histogram[E]): Double = {
    val allElements = left.elementsPresent ++ right.elementsPresent

    val similarities = for {
      element <- allElements
      leftCount = left(element)
      rightCount = right(element)
      similarity = Intersect.min(leftCount, rightCount)
    } yield similarity

    similarities.sum
  }
}
