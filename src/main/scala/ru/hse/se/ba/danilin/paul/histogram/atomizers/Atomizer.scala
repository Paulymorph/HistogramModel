package ru.hse.se.ba.danilin.paul.histogram.atomizers

import ru.hse.se.ba.danilin.paul.histogram.ElementsUniverse

trait Atomizer[S, O] extends ElementsUniverse[O] {
  def atomize(source: S): Iterable[O]
}