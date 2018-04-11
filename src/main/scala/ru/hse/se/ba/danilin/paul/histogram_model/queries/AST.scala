package ru.hse.se.ba.danilin.paul.histogram_model.queries

import ru.hse.se.ba.danilin.paul.histogram_model.histogram.{ElementsUniverse, Histogram}
import ru.hse.se.ba.danilin.paul.histogram_model.operations.{AggregateOperation, HistogramBinaryOperation, HistogramUnaryOperation}

/**
  * The base interface for a node of an AST (Abstract syntax tree) for histograms
  * @tparam E The type of the elements of the histogram
  */
sealed trait Node[E] {
  /**
    * Maps the tree to a new one node by node
    * @param f The mapper function
    * @return The result of the map
    */
  def map(f: Node[E] => Node[E]): Node[E]
}

/**
  * The node with a binary operation
  * @param operation The operation
  * @param left The left subtree
  * @param right The right subtree
  * @tparam E The type of the elements of the histogram
  */
case class BinaryOperationNode[E](operation: HistogramBinaryOperation,
                                  left: Node[E],
                                  right: Node[E]) extends Node[E] {

  override def map(f: Node[E] => Node[E]): Node[E] = f(BinaryOperationNode(operation, left.map(f), right.map(f)))
}

/**
  * The node with an unary operation
  * @param operation
  * @param histogram
  * @tparam E The type of the elements of the histogram
  */
case class UnaryOperationNode[E](operation: HistogramUnaryOperation,
                                 histogram: Node[E]) extends Node[E] {

  override def map(f: Node[E] => Node[E]): Node[E] = f(UnaryOperationNode(operation, histogram.map(f)))
}

/**
  * The node with an aggregate operation
  * @param operation The operation
  * @param left The left subtree
  * @param right The right subtree
  * @tparam E The type of the elements of the histogram
  */
case class AggregateOperationNode[E](operation: AggregateOperation,
                                     left: Node[E],
                                     right: Node[E]) extends Node[E] {

  override def map(f: Node[E] => Node[E]): Node[E] = f(AggregateOperationNode(operation, left.map(f), right.map(f)))
}

/**
  * The node with a histogram
  * @param histogram The histogram in the node
  * @tparam E The type of the elements of the histogram
  */
case class HistogramNode[E](histogram: Histogram[E]) extends Node[E] {

  override def map(f: Node[E] => Node[E]): Node[E] = f(this)
}

/**
  * The node with a subhistogram operation
  * @param properties The subhistogram
  * @param originOpt The option of a source histogram
  * @tparam E The type of the elements of the histogram
  */
case class SubhistogramNode[E](properties: ElementsUniverse[E],
                               originOpt: Option[Node[E]] = None) extends Node[E] {

  override def map(f: Node[E] => Node[E]): Node[E] = f(this)
}
