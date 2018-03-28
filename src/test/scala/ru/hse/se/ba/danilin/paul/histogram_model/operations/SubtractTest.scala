package ru.hse.se.ba.danilin.paul.histogram_model.operations

class SubtractTest extends BasicBinaryOperationTestSuite {
  def checkSubtract(a: String, b: String, res: String) =
    check(Subtract)(a, b, res)

  test("A Subtract of empty histograms should give an empty histogram") {
    checkSubtract(a = "", b = "", res = "")
  }

  test("A Subtract of something and an empty histogram should give the non empty histogram") {
    checkSubtract(a = "a b c", b = "", res = "a b c")
  }

  test("A Subtract of 'a b c d d' and 'a a b f' histograms should give the 'a b' histogram") {
    checkSubtract(a = "a b c d d", b = "a a b f", res = "c d d")
  }
}
