package ru.hse.se.ba.danilin.paul.histogram.operations

object Subtract extends HistogramBinaryMergeOperation {
  override protected def merge(leftCount: Int, rightCount: Int): Int =
    if (rightCount > 0)
      0
    else leftCount
}
