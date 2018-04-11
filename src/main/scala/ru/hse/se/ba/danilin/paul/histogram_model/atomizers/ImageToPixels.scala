package ru.hse.se.ba.danilin.paul.histogram_model.atomizers

import java.awt.image.BufferedImage
import java.awt.{Color => JColor}

class ImageToPixels extends ImageAtomizer[Color] {
  override def atomize(source: BufferedImage): Iterable[Color] = {
    val rawPixels = for {
      x <- 0 until source.getWidth
      y <- 0 until source.getHeight
      rawPixel = source.getRGB(x, y)
      pixel = new JColor(rawPixel)
    } yield pixel

    val rawParallel = rawPixels.par
    val colorsMapped = rawParallel.flatMap(Color(_))

    colorsMapped
      .toList
  }

  override def isElementInUniverse(element: Color): Boolean = true
}

sealed trait Color {
  def is(r: Int, g: Int, b: Int): Boolean
}

object Color {
  val allColors = List(Red, Green, Blue, Yellow, Magenta, Cyan)
  def apply(pixel: JColor): Iterable[Color] = {
    val (r, g, b) = (pixel.getRed, pixel.getGreen, pixel.getBlue)
    allColors.filter(pixel => pixel.is(r, g, b))
  }
}

object Red extends Color {
  override def toString: String = "RED"

  override def is(r: Int, g: Int, b: Int): Boolean =
    r == List(r, g, b).max
}

object Green extends Color {
  override def toString: String = "GREEN"

  override def is(r: Int, g: Int, b: Int): Boolean =
    g == List(r, g, b).max
}

object Blue extends Color {
  override def toString: String = "BLUE"

  override def is(r: Int, g: Int, b: Int): Boolean =
    b == List(r, g, b).max
}

abstract class SecondaryPixel extends Color {
  protected def secondaryPixel(r: Int, g: Int, b: Int): Double

  protected val threshold: Double = 2.0 / 3

  override def is(r: Int, g: Int, b: Int): Boolean = {
    secondaryPixel(r, g, b) / (r + g + b) > threshold
  }
}

object Yellow extends SecondaryPixel {
  override def toString: String = "YELLOW"

  override protected def secondaryPixel(r: Int, g: Int, b: Int): Double = r + g
}

object Magenta extends SecondaryPixel {
  override def toString: String = "MAGENTA"

  override protected def secondaryPixel(r: Int, g: Int, b: Int): Double = r + b
}

object Cyan extends SecondaryPixel {
  override def toString: String = "CYAN"

  override protected def secondaryPixel(r: Int, g: Int, b: Int): Double = g + b
}
