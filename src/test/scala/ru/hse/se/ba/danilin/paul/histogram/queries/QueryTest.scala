package ru.hse.se.ba.danilin.paul.histogram.queries

import org.scalatest.FunSuite
import ru.hse.se.ba.danilin.paul.histogram.IHistogram
import ru.hse.se.ba.danilin.paul.histogram.operations.{Subtract, Unite}

class QueryTest extends FunSuite {
  implicit val aliases = new ParserTest().aliases

  def checkQuery[E](query: String, expectation: Either[IHistogram[E], Double]) = {
    assert(Query.fromString(query).execute() == expectation)
  }

  import  ru.hse.se.ba.danilin.paul.histogram.Implicits.{stringToWords, toHistogramClass}

  test("query on 'ab + a' should give 'ab'") {
//    checkQuery("ab + a", Left("a b".toHistogram))
  }

  test("query on 'abc + a - ab' should give subtract('abc','ab')") {
//    checkQuery("abc + a - ab", Subtract("a b c".toHistogram, "a b".toHistogram))
  }

  test("query on 'abc + (a - ab)' should give a :: a :: +") {
//    checkQuery("abc + a - ab", Unite("a b c".toHistogram, Subtract("a".toHistogram, "a b".toHistogram)))
  }

}
