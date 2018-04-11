package ru.hse.se.ba.danilin.paul.histogram_model.atomizers

/**
  * A string => word atomizer
  */
class StringToWords extends StringAtomizer[String] {
  /**
    * Splits the text on words
    * @param source the source text to parse
    * @return sequence of words the source text has been split on
    */
  override def atomize(source: String) = source.split(" ;.,".toCharArray).filterNot(_ == "")

  /**
    * Whether the word is in the projection space
    * @param element the word to check
    * @return Accept all words
    */
  override def isElementInUniverse(element: String): Boolean = true
}
