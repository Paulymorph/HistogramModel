package ru.hse.se.ba.danilin.paul

import ru.hse.se.ba.danilin.paul.histogram_model.histogram.Implicits._
import ru.hse.se.ba.danilin.paul.histogram_model.queries.{HistogramInput, Input, Query}

object Main {
  def main(args: Array[String]): Unit = {
    val aliases = (Query.standardAliases[String] +
      ("blue" -> HistogramInput[String]("23487".toHistogram))+
      ("b" -> HistogramInput[String]("b".toHistogram))+
      ("c" -> HistogramInput[String]("c".toHistogram))
      ).asInstanceOf[Map[String, Input[String]]]
  }
}
