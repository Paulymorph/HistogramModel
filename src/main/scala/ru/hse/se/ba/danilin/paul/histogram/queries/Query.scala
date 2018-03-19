package ru.hse.se.ba.danilin.paul.histogram.queries

import ru.hse.se.ba.danilin.paul.histogram.{ElementsUniverse, IHistogram}
import ru.hse.se.ba.danilin.paul.histogram.operations._

class Query[E](root: Node[E]) {

  import ru.hse.se.ba.danilin.paul.histogram.Implicits._
  import ru.hse.se.ba.danilin.paul.histogram.queries.Query.Stack

  def execute(): Either[IHistogram[E], Double] = root.execute()

  def this(operationsStack: Query.Stack[Input[E]]) = {
    this(Query.parseStack[E](operationsStack))
  }
}

sealed trait Node[E] {
  def execute(): Either[IHistogram[E], Double]
}

case class BinaryOperationNode[E](operation: HistogramBinaryOperation,
                                  left: Node[E],
                                  right: Node[E]) extends Node[E] {
  override def execute(): Either[IHistogram[E], Double] =
    Left(operation(left.execute().left.get, right.execute().left.get))
}

case class UnaryOperationNode[E](operation: HistogramUnaryOperation,
                                 histogram: Node[E]) extends Node[E] {
  override def execute(): Either[IHistogram[E], Double] =
    Left(operation(histogram.execute().left.get))
}

case class AggregateOperationNode[E](operation: AggregateOperation,
                                     histogram: Node[E]) extends Node[E] {
  override def execute(): Either[IHistogram[E], Double] =
    Right(operation(histogram.execute().left.get))
}

case class HistogramNode[E](histogram: IHistogram[E]) extends Node[E] {
  override def execute(): Either[IHistogram[E], Double] =
    Left(histogram)
}

case class SubhistogramNode[E](histogram: IHistogram[E],
                               properties: ElementsUniverse[E]) extends Node[E] {
  override def execute(): Either[IHistogram[E], Double] =
    Left(histogram.subHistogram(properties))
}


object Query {

  def parseStack[E](operationsStack: Query.Stack[Input[E]]): Node[E] = {
    def parseArgument(operationsStack: Query.Stack[Input[E]]): (Node[E], Query.Stack[Input[E]]) = {
      operationsStack match {
        case HistogramInput(histogram) :: tail =>
          (HistogramNode(histogram), tail)
        case OperatorInput(operation) :: tail =>
          parseOperation(operation, tail)
      }
    }

    def parseOperation(operation: Operation,
                       operationsStack: Query.Stack[Input[E]]): (Node[E], Query.Stack[Input[E]]) = {
      operation match {
        case operation: HistogramUnaryOperation =>
          val (argument, tail) = parseArgument(operationsStack)
          (UnaryOperationNode(operation, argument), tail)
        case operation: AggregateOperation =>
          val (argument, tail) = parseArgument(operationsStack)
          (AggregateOperationNode(operation, argument), tail)
        case operation: HistogramBinaryOperation =>
          val (leftArgument, tailLeft) = parseArgument(operationsStack)
          val (rightArgument, tail) =
            tailLeft match {
              case HistogramPropertiesSetInput(subset) :: tail =>
                (SubhistogramNode(leftArgument.execute().left.get, subset), tail)

              case _ => parseArgument(tailLeft)
            }
          (BinaryOperationNode(operation, leftArgument, rightArgument), tail)
      }
    }

    operationsStack match {
      case HistogramInput(histogram) :: _ =>
        HistogramNode(histogram)
      case OperatorInput(operation) :: tail =>
        parseOperation(operation, tail)._1
    }
  }

  type Stack[E] = List[E]

  def standardAliases[E] = Stream(
    "(" -> new OpenBracketInput[E],
    ")" -> new ClosingBracketInput[E],
    "+" -> OperatorInput(Unite),
    "intersect" -> OperatorInput(Intersect),
    "-" -> OperatorInput(Subtract)
  ).toMap

  def apply[E](actionsStack: Query.Stack[Input[E]]) = new Query(actionsStack)

  def fromString[E](query: String)(implicit aliasToInput: Map[String, Input[E]]): Query[E] =
    Query(new Parser[E].parse(query)(aliasToInput).get)
}


