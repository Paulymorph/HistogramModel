package ru.hse.se.ba.danilin.paul.histogram.atomizers

class StringToWords extends IStringAtomizer[String] {
  override def atomize(source: String) = source.split(" ;.,".toCharArray).filterNot(_ == "")

  override def isElementInUniverse(element: String): Boolean = ???
}
