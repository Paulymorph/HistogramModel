package ru.hse.se.ba.danilin.paul.histogram.queries

import ru.hse.se.ba.danilin.paul.histogram.IHistogram

trait IQuery {
  def execute[E](): IHistogram[E]
}
