package ru.hse.se.ba.danilin.paul.histogram.operations
import ru.hse.se.ba.danilin.paul.histogram.IHistogram

object Similar extends AggregateOperation {
  override def apply[E](left: IHistogram[E], right: IHistogram[E]): Double = ???
}
