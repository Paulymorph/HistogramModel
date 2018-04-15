package ru.hse.se.ba.danilin.paul.histogram_model.queries

/**
  * Parser of a string to inputs
  * @tparam E The elements of a histogram
  */
class Parser[E] extends Serializable {

  import ru.hse.se.ba.danilin.paul.histogram_model.queries.Query.Stack

  /**
    * Parses the string to a polish notation stack of inputs
    * @param query The query to parse
    * @param aliasToInput The lexems to inputs for parsing
    * @return A stack of inputs in polish notation:
    */
  def parse(query: String)(implicit aliasToInput: scala.collection.Map[String, Input[E]]): Option[Stack[Input[E]]] = {
    getLexems(query.replace(" ", ""), List.empty)(aliasToInput)
      .map(toPolishNotation(_))
  }

  /**
    * Simply converts the query to a sequence of inputs
    * @param query The query to convert
    * @param acc The accumulator of the results
    * @param aliasToInput The lexems to inputs map
    * @return The sequence of inputs of the query (the first - on top)
    */
  def getLexems(query: String, acc: Stack[Input[E]])
               (implicit aliasToInput: scala.collection.Map[String, Input[E]]): Option[Stack[Input[E]]] = {
    if (query.isEmpty)
      Some(acc)
    else {
      val possibleInputs = for {
        (alias, input) <- aliasToInput if query startsWith alias
        left = query.substring(alias.length)
      } yield getLexems(left, input :: acc)(aliasToInput)

      possibleInputs.collectFirst {
        case Some(res) => res.reverse
      }
    }
  }

  /**
    * Converts the stack of inputs to a polish one
    * @param query The sequence of inputs to handle
    * @param resultAcc The resulting accumultor
    * @param operandsAcc The helping accumulator for operands
    * @return The stack of inputs converted to polish notation
    */
  def toPolishNotation(query: Stack[Input[E]],
                       resultAcc: Stack[Input[E]] = List.empty,
                       operandsAcc: Stack[Input[E]] = List.empty): Stack[Input[E]] = {
    if (query.isEmpty) {
      (operandsAcc ++ resultAcc)
        .reverse
    } else {
      val (newLexem :: left) = query
      newLexem match {
        case _: OperatorInput[E] =>
          toPolishNotation(left, newLexem :: resultAcc, operandsAcc)

        case bracket: OpenBracketInput[E] =>
          toPolishNotation(left, resultAcc, bracket :: operandsAcc)

        case _: ClosingBracketInput[E] =>
          val (beforeBracket, _ :: afterBracket) = operandsAcc.span {
            case _: OpenBracketInput[E] => false
            case _ => true
          }
          toPolishNotation(left, beforeBracket ++ resultAcc, afterBracket)

        case OperationInput(_) =>
          val (stackOperations, operandsLeft) = operandsAcc span {
            case OperationInput(_) => true
            case _ => false
          }
          toPolishNotation(left, stackOperations ++ resultAcc, newLexem::operandsLeft)
      }
    }
  }
}
