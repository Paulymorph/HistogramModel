package ru.hse.se.ba.danilin.paul

import ru.hse.se.ba.danilin.paul.histogram.Histogram
import ru.hse.se.ba.danilin.paul.histogram.operations.Unite
import ru.hse.se.ba.danilin.paul.histogram.Implicits._
import ru.hse.se.ba.danilin.paul.histogram.atomizers.StringToWords
import ru.hse.se.ba.danilin.paul.histogram.queries.{HistogramInput, Input, Query}

object Main {
  def main(args: Array[String]): Unit = {
    val aliases = (Query.standardAliases[String] + ("a" -> HistogramInput[String]("a".toHistogram))).asInstanceOf[Map[String, Input[String]]]
    val a = Query.fromString[String]("a+a")(aliases)
//    val b = Query.fromString("a+a")(aliases)
    val res = a.execute()
//    val resb = b.execute()
    print("OK")
  }
}
