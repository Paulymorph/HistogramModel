package ru.hse.se.ba.danilin.paul.histogram.queries

class Parser[E] {

  import ru.hse.se.ba.danilin.paul.histogram.queries.Query.Stack

  def parse(query: String)(implicit aliasToInput: Map[String, Input[E]]): Option[Stack[Input[E]]] = {
    getLexems(query.replace(" ", ""), List.empty)(aliasToInput)
      .map(toPolishNotation(_))
  }

  def getLexems(query: String, acc: Stack[Input[E]])
               (implicit aliasToInput: Map[String, Input[E]]): Option[Stack[Input[E]]] = {
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

  def toPolishNotation(query: Stack[Input[E]],
                       resultAcc: Stack[Input[E]] = List.empty,
                       operandsAcc: Stack[Input[E]] = List.empty): Stack[Input[E]] = {
    if (query.isEmpty) {
      (operandsAcc ++ resultAcc)
        .reverse
    } else {
      val (newLexem :: left) = query
      newLexem match {
        case _: OperandInput[E] =>
          toPolishNotation(left, newLexem :: resultAcc, operandsAcc)

        case bracket: OpenBracketInput[E] =>
          toPolishNotation(left, resultAcc, bracket :: operandsAcc)

        case _: ClosingBracketInput[E] =>
          val (beforeBracket, _ :: afterBracket) = operandsAcc.span {
            case _: OpenBracketInput[E] => false
            case _ => true
          }
          toPolishNotation(left, beforeBracket ++ resultAcc, afterBracket)

        case OperatorInput(_) =>
          val (stackOperations, operandsLeft) = operandsAcc span {
            case OperatorInput(_) => true
            case _ => false
          }
          toPolishNotation(left, stackOperations ++ resultAcc, newLexem::operandsLeft)
      }
    }
  }
}
