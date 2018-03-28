package ru.hse.se.ba.danilin.paul.histogram_model.operations

import ru.hse.se.ba.danilin.paul.histogram_model.histogram.Histogram

object Similar extends AggregateOperation {
  override def apply[E](left: Histogram[E], right: Histogram[E]): Double = ???
}
