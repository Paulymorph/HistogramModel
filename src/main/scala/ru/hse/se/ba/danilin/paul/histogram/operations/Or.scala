package ru.hse.se.ba.danilin.paul.histogram.operations

object Or extends HistogramBinaryMergeOperation {
  override def merge(leftCount: Double, rightCount: Double): Double =
    Unite.max(leftCount, rightCount)
}