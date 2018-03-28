package ru.hse.se.ba.danilin.paul.histogram_model.atomizers

import ru.hse.se.ba.danilin.paul.histogram_model.histogram.ElementsUniverse

trait Atomizer[S, O] extends ElementsUniverse[O] {
  def atomize(source: S): Iterable[O]
}