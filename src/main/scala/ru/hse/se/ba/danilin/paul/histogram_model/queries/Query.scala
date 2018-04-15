package ru.hse.se.ba.danilin.paul.histogram_model.queries

import ru.hse.se.ba.danilin.paul.histogram_model.histogram.{Histogram, ZeroHistogram}
import ru.hse.se.ba.danilin.paul.histogram_model.operations._

/**
  * AST executor
  */
object TreeExecutor extends Serializable {
  /**
    * Executes an AST
    * @param tree The root node of the AST
    * @tparam E The type of the histogram elements
    * @return The result of execution
    */
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

/**
  * Query for histogram
  * @param root The root of the AST
  * @tparam E The elements of the histogram
  */
class Query[E](root: Node[E]) extends Serializable {
  /**
    * Executes the query
    * @param histogram The histogram to execute the query on
    * @return The result of the query
    */
  def execute(histogram: Histogram[E]): Either[Histogram[E], Double] = {
    val preprocessed = injectHistogram(histogram)
    TreeExecutor.execute(preprocessed)
  }

  /**
    * Maps an elementary query to a histogram one
    * @param histogram
    * @return
    */
  def injectHistogram(histogram: Histogram[E]): Node[E] = {
    root.map {
      case SubhistogramNode(properties, None) =>
        SubhistogramNode(properties, Some(HistogramNode(histogram)))

      case node => node
    }
  }

  /**
    * Constructs the query from a sequence of inputs
    * @param inputsStack
    * @return
    */
  def this(inputsStack: Query.Stack[Input[E]]) = {
    this(Query.parseStack[E](inputsStack))
  }
}

object Query {
  /**
    * Converts the operations in polish notation to an AST
    * @param operationsStack The inputs in polish notation
    * @tparam E The type of the elements of histogram
    * @return An AST from the input
    */
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

  /**
    * Aliases for standard lexems
    * @tparam E Type of the histogram elements
    * @return Aliases for standard lexems
    */
  def standardAliases[E] = Map(
    "(" -> new OpenBracketInput[E],
    ")" -> new ClosingBracketInput[E],
    "+" -> OperationInput(Unite),
    "unite" -> OperationInput(Unite),
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

  /**
    * Factory for query creation
    * @param actionsStack The inputs in polish notation
    * @tparam E Type of the histogram elements
    * @return A constructed query
    */
  def apply[E](actionsStack: Query.Stack[Input[E]]) = new Query(actionsStack)

  /**
    * Parses a query from a string
    * @param query The string to parse
    * @param aliasToInput The lexems
    * @tparam E Type of the histogram elements
    * @return A query parsed from the string
    */
  def fromString[E](query: String)(implicit aliasToInput: scala.collection.Map[String, Input[E]]): Query[E] = {
    val polishNotationStackOpt = new Parser[E].parse(query)(aliasToInput)
    polishNotationStackOpt match {
      case None => throw new IllegalArgumentException(s"The query $query was incorrect")
      case Some(stack) => Query(stack)
    }
  }
}


