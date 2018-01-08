package ru.hse.se.ba.danilin.paul.histogram.operations

object Intersect extends HistogramBinaryOperation {
  def min(a: Int, b: Int) = if (a < b) a else b

  override protected def merge(leftCount: Int, rightCount: Int) =
    min(leftCount, rightCount)
}
