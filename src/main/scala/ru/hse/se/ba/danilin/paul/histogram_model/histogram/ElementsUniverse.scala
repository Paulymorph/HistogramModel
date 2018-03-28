package ru.hse.se.ba.danilin.paul.histogram_model.histogram

trait ElementsUniverse[E] {
  def isElementInUniverse(element: E): Boolean
}
