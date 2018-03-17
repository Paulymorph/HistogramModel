package ru.hse.se.ba.danilin.paul.histogram.operations

object Or extends HistogramBinaryMergeOperation {
  override protected def merge(leftCount: Int, rightCount: Int): Int =
    Unite.max(leftCount, rightCount)
}