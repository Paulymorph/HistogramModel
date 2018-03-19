package ru.hse.se.ba.danilin.paul.histogram.operations

object Besides extends HistogramBinaryMergeOperation {
  override protected def merge(leftCount: Double, rightCount: Double): Double =
    if (rightCount > 0)
      0
    else
      leftCount
}
