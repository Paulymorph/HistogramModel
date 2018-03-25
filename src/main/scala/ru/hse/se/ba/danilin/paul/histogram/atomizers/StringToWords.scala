package ru.hse.se.ba.danilin.paul.histogram.atomizers

class StringToWords extends StringAtomizer[String] {
  override def atomize(source: String) = source.split(" ;.,".toCharArray).filterNot(_ == "")

  override def isElementInUniverse(element: String): Boolean = ???
}
