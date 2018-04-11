package ru.hse.se.ba.danilin.paul.histogram_model.atomizers

/**
  * Interface for atomizing strings
  * @tparam E The elements type to split a string on
  */
trait StringAtomizer[E] extends Atomizer[String, E]
