package ru.hse.se.ba.danilin.paul.histogram.operations
import ru.hse.se.ba.danilin.paul.histogram.Histogram

object Similar extends AggregateOperation {
  override def apply[E](left: Histogram[E], right: Histogram[E]): Double = ???
}
