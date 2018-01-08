package ru.hse.se.ba.danilin.paul.histogram.operations

class IntersectTest extends BasicBinaryOperationTestSuite {

  def checkIntersect(a: String, b: String, res: String) =
    check(Intersect)(a, b, res)

  test("An Intersect on empty histograms should give an empty histogram") {
    checkIntersect(a = "", b = "", res = "")
  }

  test("An Intersect of something and an empty histogram should give the empty histogram") {
    checkIntersect(a = "a", b = "", res = "")
  }

  test("An Intersect of 'a b c' and 'a a b' histograms should give the 'a b' histogram") {
    checkIntersect(a = "a b c", b = "a a b", res = "a b")
  }

}
