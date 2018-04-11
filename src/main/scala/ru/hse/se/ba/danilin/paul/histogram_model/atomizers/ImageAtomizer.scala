package ru.hse.se.ba.danilin.paul.histogram_model.atomizers

import java.awt.image.BufferedImage

/**
  * Interface for atomizing images
  * @tparam E The elements type to split image on
  */
trait ImageAtomizer[E] extends Atomizer[BufferedImage, E]
