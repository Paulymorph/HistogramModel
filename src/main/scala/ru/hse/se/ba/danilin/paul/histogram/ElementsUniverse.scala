package ru.hse.se.ba.danilin.paul.histogram

trait ElementsUniverse[E] {
  def isElementInUniverse(element: E): Boolean
}
