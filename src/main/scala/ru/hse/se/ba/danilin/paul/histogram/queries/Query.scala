package ru.hse.se.ba.danilin.paul.histogram.queries
import ru.hse.se.ba.danilin.paul.histogram.IHistogram
import ru.hse.se.ba.danilin.paul.histogram.operations.Operation

sealed trait Input

final case class HistogramPropertiesSetInput[E](properties: Set[E]) extends Input

final case class HistogramInput[E](histogram: IHistogram[E]) extends Input

final case class OperationInput(operation: Operation) extends Input

object Query {
  def fromString(query: String, aliasToInput: Map[String, Input]): Input  = ???
}
