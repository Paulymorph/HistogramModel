package ru.hse.se.ba.danilin.paul.histogram.queries

import ru.hse.se.ba.danilin.paul.histogram.IHistogram
import ru.hse.se.ba.danilin.paul.histogram.operations._

sealed trait Input

object Input {
  def apply[T, E](t: T): Input =
    t match {
      case hist: IHistogram[E] => HistogramInput(hist)
      case props: Set[E] => HistogramPropertiesSetInput(props)
      case _ => AggregateInput(t)
    }
}

sealed trait OperandInput extends Input

final case class OperatorInput(operation: Operation) extends Input

final case class HistogramPropertiesSetInput[E](properties: Set[E]) extends OperandInput

final case class HistogramInput[E](histogram: IHistogram[E]) extends OperandInput

final case class AggregateInput[T](obj: T) extends OperandInput

object OpenBracketInput extends Input

object ClosingBracketInput extends Input

case class Query(actionsStack: Query.Stack[Input]) {

  import ru.hse.se.ba.danilin.paul.histogram.Implicits._
  import ru.hse.se.ba.danilin.paul.histogram.queries.Query.Stack

  def execute(): Either[IHistogram[Any], Double] = {
    def processOperation[E](operation: Operation, processingStack: Stack[Input]) =
      operation match {
        case bOp: HistogramBinaryOperation =>
          val (HistogramInput(a) :: b :: processingStackLeft) = processingStack
          val operationResult = b match {
            case HistogramInput(b) => bOp(a, b)
            case HistogramPropertiesSetInput(b) => bOp(a, b)
          }
          (Input(operationResult), processingStackLeft)

        case op: HistogramUnaryOperation =>
          val (HistogramInput(a) :: processingStackLeft) = processingStack
          (Input(op(a)), processingStackLeft)
      }

    def innerExecute(queryStack: Stack[Input], processingStack: Stack[Input]): Stack[Input] =
      if (queryStack.isEmpty) {
        processingStack
      } else {
        val (curInput :: leftQuery) = queryStack
        curInput match {
          case _: OperandInput =>
            innerExecute(leftQuery, curInput :: processingStack)
          case OperatorInput(operation) =>
            val (operationResult, processingStackLeft) = processOperation(operation, processingStack)
            innerExecute(leftQuery, operationResult :: processingStackLeft)
        }
      }

    val resultStack = innerExecute(actionsStack, List.empty)
    val result = resultStack.head
    result match {
      case HistogramInput(histogram) => Left(histogram)
      case AggregateInput(number: Double) => Right(number)
    }
  }


  val aliasesToInput = Stream(
    "(" -> OpenBracketInput,
    ")" -> ClosingBracketInput,
    "+" -> OperatorInput(Unite),
    "intersect" -> OperatorInput(Intersect),
    "-" -> OperatorInput(Subtract),
    "ab" -> HistogramInput("a b".toHistogram),
    "abc" -> HistogramInput("a b c".toHistogram),
    "abb" -> HistogramInput("a b b".toHistogram)
  ).toMap
}

object Query {
  type Stack[E] = List[E]

  def fromString(query: String)(implicit aliasToInput: Map[String, Input]): Query =
    Query(Parser.parse(query)(aliasToInput).get)
}


