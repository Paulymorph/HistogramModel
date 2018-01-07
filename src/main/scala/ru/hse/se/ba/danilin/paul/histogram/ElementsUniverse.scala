package ru.hse.se.ba.danilin.paul.histogram

abstract class ElementsUniverse[E] {
  def isElementInUniverse(element: E): Boolean
}
