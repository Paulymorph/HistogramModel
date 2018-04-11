package ru.hse.se.ba.danilin.paul.histogram_model.operations

/**
  * The BESIDES operation
  */
object Besides extends HistogramBinaryMergeOperation {
  /**
    * Merges two elements
    * @param leftCount The count of an element from the left histogram
    * @param rightCount The count of an element from the right histogram
    * @return The result count of the element in the resulting histogram
    */
  override def merge(leftCount: Double, rightCount: Double): Double =
    if (rightCount > 0)
      0
    else
      leftCount
}
