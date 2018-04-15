package ru.hse.se.ba.danilin.paul.histogram_model.histogram

/**
  * Universe of elements
  * @tparam E The element type
  */
trait ElementsUniverse[E] extends Serializable {
  /**
    * Whether the element is in universe
    * @param element The element to check
    * @return True if the element is in universe, false otherwise
    */
  def isElementInUniverse(element: E): Boolean

  def allElements: Seq[E]
}
