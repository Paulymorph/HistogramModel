package ru.hse.se.ba.danilin.paul.histogram.queries

object Parser {

  import ru.hse.se.ba.danilin.paul.histogram.queries.Query.Stack

  def parse(query: String)(implicit aliasToInput: Map[String, Input]): Option[Stack[Input]] = {
    getLexems(query.replace(" ", ""), List.empty)(aliasToInput)
      .map(toPolishNotation(_))
  }

  def getLexems(query: String, acc: Stack[Input])
               (implicit aliasToInput: Map[String, Input]): Option[Stack[Input]] = {
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

  def toPolishNotation(query: Stack[Input],
                       resultAcc: Stack[Input] = List.empty,
                       operandsAcc: Stack[Input] = List.empty): Stack[Input] = {
    if (query.isEmpty) {
      (operandsAcc ++ resultAcc)
        .reverse
    } else {
      val (newLexem :: left) = query
      newLexem match {
        case _: OperandInput =>
          toPolishNotation(left, newLexem :: resultAcc, operandsAcc)

        case OpenBracketInput =>
          toPolishNotation(left, resultAcc, newLexem :: operandsAcc)

        case ClosingBracketInput =>
          val (beforeBracket, _ :: afterBracket) = operandsAcc.span {
            case OpenBracketInput => false
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
