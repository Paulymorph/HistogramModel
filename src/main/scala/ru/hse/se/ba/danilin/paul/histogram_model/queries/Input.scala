package ru.hse.se.ba.danilin.paul.histogram_model.queries

import ru.hse.se.ba.danilin.paul.histogram_model.histogram.{ElementsUniverse, Histogram}
import ru.hse.se.ba.danilin.paul.histogram_model.operations.Operation

sealed trait Input[E]

object Input {
  def apply[T, E](t: T): Input[E] =
    t match {
      case hist: Histogram[E] => HistogramInput(hist)
      case props: ElementsUniverse[E] => HistogramPropertiesSetInput(props)
//      case _ => AggregateInput(t)
    }
}

sealed trait OperandInput[E] extends Input[E]

final case class OperatorInput[E](operation: Operation) extends Input[E]

final case class HistogramPropertiesSetInput[E](properties: ElementsUniverse[E]) extends OperandInput[E]

final case class HistogramInput[E](histogram: Histogram[E]) extends OperandInput[E]

//final case class AggregateInput[T](obj: T) extends OperandInput

final class OpenBracketInput[E] extends Input[E]

final class ClosingBracketInput[E] extends Input[E]
