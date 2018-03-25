package ru.hse.se.ba.danilin.paul.histogram.queries

import ru.hse.se.ba.danilin.paul.histogram.{ElementsUniverse, Histogram}
import ru.hse.se.ba.danilin.paul.histogram.operations.{AggregateOperation, HistogramBinaryOperation, HistogramUnaryOperation}

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

case class HistogramNode[E](histogram: Histogram[E]) extends Node[E] {

  override def map(f: Node[E] => Node[E]): Node[E] = f(this)
}

case class SubhistogramNode[E](properties: ElementsUniverse[E],
                               originOpt: Option[Histogram[E]] = None) extends Node[E] {

  override def map(f: Node[E] => Node[E]): Node[E] = f(this)
}
