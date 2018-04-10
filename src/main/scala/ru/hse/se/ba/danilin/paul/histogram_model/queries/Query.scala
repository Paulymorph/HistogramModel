package ru.hse.se.ba.danilin.paul.histogram_model.queries

import ru.hse.se.ba.danilin.paul.histogram_model.histogram.{Histogram, ZeroHistogram}
import ru.hse.se.ba.danilin.paul.histogram_model.operations._

object TreeExecutor {
  def execute[E](tree: Node[E]): Either[Histogram[E], Double] = {
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

      case SubhistogramNode(properties, originNode) =>
        val origin = originNode.map(execute(_).left.get)
        Left(origin.map(_.subHistogram(properties)).getOrElse(new ZeroHistogram()(properties)))
    }
  }
}

class Query[E](root: Node[E]) {
  def execute(histogram: Histogram[E]): Either[Histogram[E], Double] = {
    val preprocessed = injectHistogram(histogram)
    TreeExecutor.execute(preprocessed)
  }

  def injectHistogram(histogram: Histogram[E]): Node[E] = {
    root.map {
      case SubhistogramNode(properties, None) =>
        SubhistogramNode(properties, Some(HistogramNode(histogram)))

      case node => node
    }
  }

  def this(operationsStack: Query.Stack[Input[E]]) = {
    this(Query.parseStack[E](operationsStack))
  }
}

object Query {

  def parseStack[E](operationsStack: Stack[Input[E]]): Node[E] = {
    def innerParse(operationsStack: Stack[Input[E]],
                      argumentsStack: Stack[Node[E]] = List.empty): Stack[Node[E]] = {
      operationsStack match {
        case Nil => argumentsStack

        case HistogramInput(hist) :: tail => innerParse(tail, HistogramNode(hist) :: argumentsStack)

        case SubhistogramInput(properties) :: tail =>
          innerParse(tail, SubhistogramNode(properties) :: argumentsStack)

        case OperationInput(operator) :: operationsTail =>
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

  def standardAliases[E] = Map(
    "(" -> new OpenBracketInput[E],
    ")" -> new ClosingBracketInput[E],
    "+" -> OperationInput(Unite),
    "intersect" -> OperationInput(Intersect),
    "-" -> OperationInput(Subtract),
    "&" -> OperationInput(And),
    "|" -> OperationInput(Or),
    "xor" -> OperationInput(Xor),
    "not" -> OperationInput(Not),
    "besides" -> OperationInput(Besides),
    "xbesides" -> OperationInput(XBesides),
    "sim" -> OperationInput(Similar)
  )

  def apply[E](actionsStack: Query.Stack[Input[E]]) = new Query(actionsStack)

  def fromString[E](query: String)(implicit aliasToInput: Map[String, Input[E]]): Query[E] =
    Query(new Parser[E].parse(query)(aliasToInput).get)
}


