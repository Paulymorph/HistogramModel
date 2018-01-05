package ru.hse.se.ba.danilin.paul.histogram.atomizers

trait IAtomizer[S, O] extends IElementsUniverse[O] {
  def atomize(source: S): Iterable[O]
}