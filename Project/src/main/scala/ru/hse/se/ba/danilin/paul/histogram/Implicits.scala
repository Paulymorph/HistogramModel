package ru.hse.se.ba.danilin.paul.histogram

object Implicits {
  implicit class toHistogramClass[S](source: S) {
    def toHistogram = Histogram.extract(source)
  }
}
