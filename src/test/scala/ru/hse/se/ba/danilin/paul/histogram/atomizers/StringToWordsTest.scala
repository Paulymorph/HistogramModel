package ru.hse.se.ba.danilin.paul.histogram.atomizers

import org.scalatest.FunSuite

class StringToWordsTest extends FunSuite {
  val atomizer = new StringToWords

  test("atomize of an empty string should atomize to empty sequence") {
    assert(atomizer.atomize("") == Seq())
  }

  test("atomize of 'a b word.word2' should atomize to Seq(a, b, word, word2)") {
    assert(atomizer.atomize("a b word.word2") == Seq("a", "b", "word", "word2"))
  }
}
