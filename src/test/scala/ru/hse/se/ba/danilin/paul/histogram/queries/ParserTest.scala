package ru.hse.se.ba.danilin.paul.histogram.queries

import org.scalatest.FunSuite
import ru.hse.se.ba.danilin.paul.histogram.operations.{Intersect, Subtract, Unite}

class ParserTest extends FunSuite {

  import ru.hse.se.ba.danilin.paul.histogram.Implicits.{toHistogramClass, stringToWords}

  val aliases = Seq(
    "(" -> new OpenBracketInput[String],
    ")" -> new ClosingBracketInput[String],
    "+" -> OperatorInput[String](Unite),
    "intersect" -> OperatorInput[String](Intersect),
    "-" -> OperatorInput[String](Subtract),
    "a" -> HistogramInput[String]("a".toHistogram),
    "ab" -> HistogramInput[String]("a b".toHistogram),
    "abc" -> HistogramInput[String]("a b c".toHistogram),
    "abb" -> HistogramInput[String]("a b b".toHistogram)
  ).toMap

  def checkParser(query: String, expectation: List[String]) = {
    val expectationToInputs = expectation map (aliases(_))
    assert(new Parser[String].parse(query)(aliases).contains(expectationToInputs))
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
