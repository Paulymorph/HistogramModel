package ru.hse.se.ba.danilin.paul.histogram_model.atomizers

import ru.hse.se.ba.danilin.paul.histogram_model.histogram.ElementsUniverse

/**
  * Interface for parsing object to Atomic elements
  * @tparam S The source type
  * @tparam O The type to project on
  */
trait Atomizer[S, O] extends ElementsUniverse[O] {
  /**
    * Splits the object on elements
    * @param source The source to parse
    * @return Sequence of elements the source has been split on
    */
  def atomize(source: S): Iterable[O]
}