package ru.hse.se.ba.danilin.paul.histogram.operations

object Intersect extends HistogramBinaryMergeOperation {
  def min(a: Double, b: Double) = if (a < b) a else b

  override def merge(leftCount: Double, rightCount: Double) =
    min(leftCount, rightCount)
}
