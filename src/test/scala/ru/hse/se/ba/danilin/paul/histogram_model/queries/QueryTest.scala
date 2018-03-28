package ru.hse.se.ba.danilin.paul.histogram_model.queries

import org.scalatest.FunSuite
import ru.hse.se.ba.danilin.paul.histogram_model.histogram.Implicits._
import ru.hse.se.ba.danilin.paul.histogram_model.histogram.Histogram

class QueryTest extends FunSuite {
  val histogram: Histogram[String] = "a b c a a b b a a".toHistogram

  val aSub = Set("a")
  val bSub = Set("b")
  val cSub = Set("c")

  implicit val aliases: Map[String, Input[String]] = (Query.standardAliases +
      ("a" -> HistogramPropertiesSetInput(aSub)) +
      ("b" -> HistogramPropertiesSetInput(bSub)) +
      ("c" -> HistogramPropertiesSetInput(cSub))).asInstanceOf[Map[String, Input[String]]]

  def checkQuery(queryString: String, expectation: Either[Histogram[String], Double], histogram: Histogram[String] = histogram) = {
    val query = Query.fromString(queryString)
    val result = query.execute(histogram)
    assert(result == expectation)
  }

  test("query on 'b + a' should be correct") {
    val expectation = histogram.subHistogram(bSub) + histogram.subHistogram(aSub)
    checkQuery("b + a", Left(expectation))
  }

  test("query on 'a + c - b' should be correct") {
    val expectation = histogram.subHistogram(aSub) + histogram.subHistogram(cSub) - histogram.subHistogram(bSub)
    checkQuery("a + c - b", Left(expectation))
  }

  test("query on 'a + (c - b)' should be correct") {
    val expectation = histogram.subHistogram(aSub) + (histogram.subHistogram(cSub) - histogram.subHistogram(bSub))
    checkQuery("a + (c - b)", Left(expectation))
  }

}
