package ru.hse.se.ba.danilin.paul.histogram.operations

import org.scalatest.FunSuite
import ru.hse.se.ba.danilin.paul.histogram.Implicits.toHistogramClass
import ru.hse.se.ba.danilin.paul.histogram.atomizers.{IStringAtomizer, StringToWords}

class BasicBinaryOperationTestSuite extends FunSuite {
  implicit val stringToWords: IStringAtomizer[String] = new StringToWords

  def check(operation: HistogramBinaryOperation)(a: String, b: String, res: String) = {
    assert(operation(a.toHistogram, b.toHistogram) == res.toHistogram)
  }
}
