package ru.hse.se.ba.danilin.paul.histogram.queries

import ru.hse.se.ba.danilin.paul.histogram.{ElementsUniverse, IHistogram}
import ru.hse.se.ba.danilin.paul.histogram.operations._

object TreeExecutor {
  def execute[E](tree: Node[E]): Either[IHistogram[E], Double] = {
    def extract(node: Node[E]) = {
      execute(node).left.get
    }

    tree match {
      case BinaryOperationNode(operation, left, right) =>
        Left(operation(extract(left), extract(right)))
      case UnaryOperationNode(operation, histogram) =>
        Left(operation(extract(histogram)))
      case AggregateOperationNode(operation, left, right) =>
        Right(operation(extract(left), extract(right)))
      case HistogramNode(histogram) =>
        Left(histogram)
    }
  }
}

class Query[E](root: Node[E]) {

  import ru.hse.se.ba.danilin.paul.histogram.Implicits._

  def execute(histogram: IHistogram[E]): Either[IHistogram[E], Double] = {
    val preprocessed = root.map {
      case SubhistogramNode(properties, None) => SubhistogramNode(properties, Some(histogram))
      case node => node
    }
    TreeExecutor.execute(preprocessed)
  }

  def this(operationsStack: Query.Stack[Input[E]]) = {
    this(Query.parseStack[E](operationsStack))
  }
}

sealed trait Node[E] {
  def map(f: Node[E] => Node[E]): Node[E]
}

case class BinaryOperationNode[E](operation: HistogramBinaryOperation,
                                  left: Node[E],
                                  right: Node[E]) extends Node[E] {

  override def map(f: Node[E] => Node[E]): Node[E] = f(BinaryOperationNode(operation, left.map(f), right.map(f)))
}

case class UnaryOperationNode[E](operation: HistogramUnaryOperation,
                                 histogram: Node[E]) extends Node[E] {

  override def map(f: Node[E] => Node[E]): Node[E] = f(UnaryOperationNode(operation, histogram.map(f)))
}

case class AggregateOperationNode[E](operation: AggregateOperation,
                                     left: Node[E],
                                     right: Node[E]) extends Node[E] {

  override def map(f: Node[E] => Node[E]): Node[E] = f(AggregateOperationNode(operation, left.map(f), right.map(f)))
}

case class HistogramNode[E](histogram: IHistogram[E]) extends Node[E] {

  override def map(f: Node[E] => Node[E]): Node[E] = f(this)
}

case class SubhistogramNode[E](properties: ElementsUniverse[E],
                               origin: Option[IHistogram[E]] = None) extends Node[E] {

  override def map(f: Node[E] => Node[E]): Node[E] = f(this)
}


object Query {

  def parseStack[E](operationsStack: Stack[Input[E]]): Node[E] = {
    def innerParse(operationsStack: Stack[Input[E]],
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


