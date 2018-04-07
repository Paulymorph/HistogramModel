package ru.hse.se.ba.danilin.paul.histogram_model.atomizers
import java.awt.Color
import java.awt.image.BufferedImage

class ImageToPixels extends ImageAtomizer[Pixel] {
  override def atomize(source: BufferedImage): Iterable[Pixel] = {
    for {
      x <- 0 until source.getWidth
      y <- 0 until source.getHeight
      rawPixel = source.getRGB(x, y)
      pixel = new Color(rawPixel)
    } yield Pixel(pixel)
  }

  override def isElementInUniverse(element: Pixel): Boolean = true
}

sealed trait Pixel

object Pixel {
  def apply(pixel: Color): Pixel = {
    val (r, g, b) = (pixel.getRed, pixel.getGreen, pixel.getBlue)
    val maxColorValue = List(r, g, b).max
    maxColorValue match {
      case `r` => Red
      case `g` => Green
      case `b` => Blue
    }
  }
}

object Red extends Pixel {
  override def toString: String = "RED"
}

object Green extends Pixel {
  override def toString: String = "GREEN"
}

object Blue extends Pixel {
  override def toString: String = "BLUE"
}
