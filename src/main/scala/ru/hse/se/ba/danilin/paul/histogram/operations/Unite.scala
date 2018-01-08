package ru.hse.se.ba.danilin.paul.histogram.operations

object Unite extends HistogramBinaryOperation {
  def max = (a: Int, b: Int) => if (a > b) a else b

  override protected def merge(leftCount: Int, rightCount: Int): Int =
    max(leftCount, rightCount)
}
