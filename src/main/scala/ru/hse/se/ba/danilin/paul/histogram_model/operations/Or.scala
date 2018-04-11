package ru.hse.se.ba.danilin.paul.histogram_model.operations

/**
  * The OR operation
  */
object Or extends HistogramBinaryMergeOperation {
  override def merge(leftCount: Double, rightCount: Double): Double =
    Unite.max(leftCount, rightCount)
}