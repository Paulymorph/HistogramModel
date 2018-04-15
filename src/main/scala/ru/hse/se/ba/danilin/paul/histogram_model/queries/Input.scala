package ru.hse.se.ba.danilin.paul.histogram_model.queries

import ru.hse.se.ba.danilin.paul.histogram_model.histogram.{ElementsUniverse, Histogram}
import ru.hse.se.ba.danilin.paul.histogram_model.operations.Operation

/**
  * Interface for inputs of a query
  * @tparam E The type of the elements of a histogram
  */
sealed trait Input[E] extends Serializable

/**
  * A base for common lexems
  * @tparam E The type of the elements of a histogram
  */
sealed trait OperatorInput[E] extends Input[E]

/**
  * An operation input
  * @param operation The operation in the input
  * @tparam E The type of the elements of a histogram
  */
final case class OperationInput[E](operation: Operation) extends Input[E]

/**
  * Subhistogram input
  * @param properties The subhistogram
  * @tparam E The type of the elements of a histogram
  */
final case class SubhistogramInput[E](properties: ElementsUniverse[E]) extends OperatorInput[E]

/**
  * Histogram input
  * @param histogram The histogram
  * @tparam E The type of the elements of a histogram
  */
final case class HistogramInput[E](histogram: Histogram[E]) extends OperatorInput[E]

/**
  * The opening bracket
  * @tparam E The type of the elements of a histogram
  */
final class OpenBracketInput[E] extends Input[E]

/**
  * The closing bracket
  * @tparam E The type of the elements of a histogram
  */
final class ClosingBracketInput[E] extends Input[E]
