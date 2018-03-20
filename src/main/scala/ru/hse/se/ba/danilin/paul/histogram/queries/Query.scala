package ru.hse.se.ba.danilin.paul.histogram.queries

import ru.hse.se.ba.danilin.paul.histogram.{ElementsUniverse, IHistogram}
import ru.hse.se.ba.danilin.paul.histogram.operations._

class Query[E](root: Node[E]) {

  import ru.hse.se.ba.danilin.paul.histogram.Implicits._

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

  def parseStack[E](operationsStack: Stack[Input[E]]): Node[E] = {
    def innerParse[E](operationsStack: Stack[Input[E]],
                      argumentsStack: Stack[Node[E]] = List.empty): Stack[Node[E]] = {
      operationsStack match {
        case Nil => argumentsStack
        case HistogramInput(hist) :: tail => innerParse(tail, HistogramNode(hist) :: argumentsStack)
        case OperatorInput(operator) :: operationsTail =>
          operator match {
            case op: HistogramUnaryOperation =>
              argumentsStack match {
                case head :: argumentsTail =>
                  innerParse(operationsTail, UnaryOperationNode(op, head) :: argumentsTail)
              }
            case op: HistogramBinaryOperation =>
              argumentsStack match {
                case second :: first :: argumentsTail =>
                  innerParse(operationsTail, BinaryOperationNode(op, first, second) :: argumentsTail)
              }
          }
      }
    }
    innerParse(operationsStack).head
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


