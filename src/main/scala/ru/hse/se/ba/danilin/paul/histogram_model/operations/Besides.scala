package ru.hse.se.ba.danilin.paul.histogram_model.operations

object Besides extends HistogramBinaryMergeOperation {
  override def merge(leftCount: Double, rightCount: Double): Double =
    if (rightCount > 0)
      0
    else
      leftCount
}
