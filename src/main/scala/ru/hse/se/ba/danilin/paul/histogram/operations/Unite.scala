package ru.hse.se.ba.danilin.paul.histogram.operations

object Unite extends HistogramBinaryMergeOperation {
  def max = (a: Double, b: Double) => if (a > b) a else b

  override def merge(leftCount: Double, rightCount: Double): Double =
    max(leftCount, rightCount)
}
