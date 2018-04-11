package ru.hse.se.ba.danilin.paul.histogram_model.operations

/**
  * The SUBTRACT operation
  */
object Subtract extends HistogramBinaryMergeOperation {
  override def merge(leftCount: Double, rightCount: Double): Double =
    Besides.merge(leftCount, rightCount)
}
