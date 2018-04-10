package ru.hse.se.ba.danilin.paul.histogram_model.atomizers
import java.awt.Color
import java.awt.image.BufferedImage

class ImageToPixels extends ImageAtomizer[Pixel] {
  override def atomize(source: BufferedImage): Iterable[Pixel] = {
    (for {
      x <- 0 until source.getWidth
      y <- 0 until source.getHeight
      rawPixel = source.getRGB(x, y)
      pixel = new Color(rawPixel)
    } yield Pixel(pixel)).flatten
  }

  override def isElementInUniverse(element: Pixel): Boolean = true
}

sealed trait Pixel {
  def is(r: Int, g: Int, b: Int): Boolean
}

object Pixel {
  def apply(pixel: Color): Iterable[Pixel] = {
    val (r, g, b) = (pixel.getRed, pixel.getGreen, pixel.getBlue)
    val candidates = List(Red, Green, Blue, Yellow, Magenta, Cyan)
    candidates.filter(pixel => pixel.is(r, g, b))
  }
}

object Red extends Pixel {
  override def toString: String = "RED"

  override def is(r: Int, g: Int, b: Int): Boolean =
    r == List(r, g, b).max
}

object Green extends Pixel {
  override def toString: String = "GREEN"

  override def is(r: Int, g: Int, b: Int): Boolean =
    g == List(r, g, b).max
}

object Blue extends Pixel {
  override def toString: String = "BLUE"

  override def is(r: Int, g: Int, b: Int): Boolean =
    b == List(r, g, b).max
}

abstract class SecondaryPixel extends Pixel {
  protected def secondaryPixel(r: Int, g: Int, b: Int): Double

  protected val threshold: Double = 2 / 3

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
