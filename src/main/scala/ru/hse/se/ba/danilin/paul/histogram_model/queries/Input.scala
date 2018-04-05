package ru.hse.se.ba.danilin.paul.histogram_model.queries

import ru.hse.se.ba.danilin.paul.histogram_model.histogram.{ElementsUniverse, Histogram}
import ru.hse.se.ba.danilin.paul.histogram_model.operations.Operation

sealed trait Input[E]

sealed trait OperatorInput[E] extends Input[E]

final case class OperationInput[E](operation: Operation) extends Input[E]

final case class SubhistogramInput[E](properties: ElementsUniverse[E]) extends OperatorInput[E]

final case class HistogramInput[E](histogram: Histogram[E]) extends OperatorInput[E]

//final case class AggregateInput[T](obj: T) extends OperandInput

final class OpenBracketInput[E] extends Input[E]

final class ClosingBracketInput[E] extends Input[E]
