package ru.hse.se.ba.danilin.paul.histogram_model.operations

import org.scalatest.FunSuite
import ru.hse.se.ba.danilin.paul.histogram_model.histogram.Implicits.toHistogramClass
import ru.hse.se.ba.danilin.paul.histogram_model.atomizers.{StringAtomizer, StringToWords}

class BasicBinaryOperationTestSuite extends FunSuite {
  implicit val stringToWords: StringAtomizer[String] = new StringToWords

  def check(operation: HistogramBinaryOperation)(a: String, b: String, res: String) = {
    assert(operation(a.toHistogram, b.toHistogram) == res.toHistogram)
  }
}
