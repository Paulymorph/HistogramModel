package ru.hse.se.ba.danilin.paul.histogram.operations

class UniteTest extends BasicBinaryOperationTestSuite {
  def checkUnite(a: String, b: String, res: String) = check(Unite)(a, b, res)

  test("A Unite on empty histograms should give an empty histogram") {
    checkUnite(a = "", b = "", res = "")
  }

  test("A Unite on something and an empty histogram should give the non empty histogram") {
    checkUnite(a = "a", b = "", res = "a")
  }

  test("A Unite of 'a b c' and 'a a b' histograms should give the 'a a b c' histogram") {
    checkUnite(a = "a b c", b = "a a b", res = "a a b c")
  }
}
