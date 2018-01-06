package ru.hse.se.ba.danilin.paul.histogram.queries
import ru.hse.se.ba.danilin.paul.histogram.IHistogram
import ru.hse.se.ba.danilin.paul.histogram.operations.HistogramOperation

case class Query(operation: HistogramOperation) extends IQuery {
  override def execute[E]() = ???
}

object Query {
  def fromString(query: String): Query = ???
}