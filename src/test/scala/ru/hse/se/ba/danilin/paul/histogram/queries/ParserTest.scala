package ru.hse.se.ba.danilin.paul.histogram.queries

import org.scalatest.FunSuite
import ru.hse.se.ba.danilin.paul.histogram.operations.{Intersect, Subtract, Unite}

class ParserTest extends FunSuite {

  import ru.hse.se.ba.danilin.paul.histogram.Implicits.{toHistogramClass, stringToWords}

  implicit val aliases = Seq(
    "(" -> OpenBracketInput,
    ")" -> ClosingBracketInput,
    "+" -> OperatorInput(Unite),
    "intersect" -> OperatorInput(Intersect),
    "-" -> OperatorInput(Subtract),
    "a" -> HistogramInput("a".toHistogram),
    "ab" -> HistogramInput("a b".toHistogram),
    "abc" -> HistogramInput("a b c".toHistogram),
    "abb" -> HistogramInput("a b b".toHistogram)
  ).toMap

  def checkParser(query: String, expectation: List[String]) = {
    val expectationToInputs = expectation map (aliases(_))
    assert(Parser.parse(query).contains(expectationToInputs))
  }

  test("parse on 'ab + a' should give ab :: a :: +") {
    checkParser("ab + a", List("ab", "a", "+"))
  }

  test("parse on 'abc + a - ab' should give abc :: a :: + :: ab :: -") {
    checkParser("abc + a - ab", List("abc", "a", "+", "ab", "-"))
  }

  test("parse on 'abc + (a - ab)' should give a :: a :: +") {
    checkParser("abc + (a - ab)", List("abc", "a", "ab", "-", "+"))
  }
}
