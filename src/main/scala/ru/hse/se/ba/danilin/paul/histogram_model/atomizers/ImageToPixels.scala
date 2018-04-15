package ru.hse.se.ba.danilin.paul.histogram_model.atomizers

import java.awt.image.BufferedImage
import java.awt.{Color => JColor}

/**
  * Atomizer Image => Color of pixels
  */
class ImageToPixels extends ImageAtomizer[Color] {
  /**
    * Parses an image
    *
    * @param source The source image to parse
    * @return Sequence of pixels colors the source has been split on
    */
  override def atomize(source: BufferedImage): Iterable[Color] = {
    val rawPixels = TimeUtils.time({
      for {
        x <- 0 until source.getWidth
        y <- 0 until source.getHeight
        rawPixel = source.getRGB(x, y)
        pixel = new JColor(rawPixel)
      } yield pixel
    })

    TimeUtils.time({
      val rawParallel = rawPixels.par
      val colorsMapped = rawParallel.flatMap(Color(_))

      colorsMapped
        .toList
    })
  }

  /**
    * Finds out if the Color is in the projection space
    *
    * @param element The element to check
    * @return True for any color
    */
  override def isElementInUniverse(element: Color): Boolean = true
}

/**
  * The color of pixel
  */
sealed trait Color extends Serializable {
  protected def max(r: Int, g: Int, b: Int) = {
    if (r > g) {
      if (r > b)
        r
      else
        b
    } else if (g > b)
      g
    else b
  }

  /**
    * Whether the pixel is of the color
    *
    * @param r The red component of the pixel
    * @param g The green component of the pixel
    * @param b The blue component of the pixel
    * @return True if the pixel is of the color, false otherwise
    */
  def is(r: Int, g: Int, b: Int): Boolean
}

object Color {
  /**
    * All colors possible
    */
  val allColors = List(Red, LightBlue, Blue, Yellow, GreenYellow, Green)

  /**
    * Finds out the colors of the pixel
    *
    * @param pixel The pixel to check colors of
    * @return The colors of the pixel
    */
  def apply(pixel: JColor) = {
    val r = pixel.getRed
    val g = pixel.getGreen
    val b = pixel.getBlue
    allColors.filter(pixel => pixel.is(r, g, b))
  }
}

/**
  * Red color
  */
object Red extends Color {
  override def toString: String = "RED"

  /**
    * If the pixel is red
    *
    * @param r The red component of the pixel
    * @param g The green component of the pixel
    * @param b The blue component of the pixel
    * @return True if the pixel is of the color, false otherwise
    */
  override def is(r: Int, g: Int, b: Int): Boolean =
    r == max(r, g, b)
}

/**
  * Green color
  */
object Green extends Color {
  override def toString: String = "GREEN"

  /**
    * If the pixel is green
    *
    * @param r The red component of the pixel
    * @param g The green component of the pixel
    * @param b The blue component of the pixel
    * @return True if the pixel is of the color, false otherwise
    */
  override def is(r: Int, g: Int, b: Int): Boolean =
    g == max(r, g, b)
}

/**
  * Blue color
  */
object Blue extends Color {
  override def toString: String = "BLUE"

  /**
    * If the pixel is blue
    *
    * @param r The red component of the pixel
    * @param g The green component of the pixel
    * @param b The blue component of the pixel
    * @return True if the pixel is of the color, false otherwise
    */
  override def is(r: Int, g: Int, b: Int): Boolean =
    b == max(r, g, b)
}

/**
  * Base class for secondary colors
  */
abstract class SecondaryPixel extends Color {
  /**
    * The "certainty" of the secondary color
    *
    * @param r The red component of the pixel
    * @param g The green component of the pixel
    * @param b The blue component of the pixel
    * @return A number representing the certainty of the pixel being of the color
    */
  protected def secondaryPixel(r: Int, g: Int, b: Int): Double

  /**
    * Threshold for decision
    */
  protected val threshold: Double = 2.0 / 3

  /**
    * If the pixel of the secondary color
    *
    * @param r The red component of the pixel
    * @param g The green component of the pixel
    * @param b The blue component of the pixel
    * @return True if the pixel is of the color, false otherwise
    */
  override def is(r: Int, g: Int, b: Int): Boolean = {
    secondaryPixel(r, g, b) / (r + g + b) > threshold
  }
}

/**
  * Yellow color
  */
object Yellow extends SecondaryPixel {
  override def toString: String = "YELLOW"

  /**
    * The yellow component
    *
    * @param r The red component of the pixel
    * @param g The green component of the pixel
    * @param b The blue component of the pixel
    * @return A number representing the certainty of the pixel being of the color
    */
  override protected def secondaryPixel(r: Int, g: Int, b: Int): Double = r + g
}

/**
  * Magenta color
  */
object Magenta extends SecondaryPixel {
  override def toString: String = "MAGENTA"

  /**
    * The magenta component
    *
    * @param r The red component of the pixel
    * @param g The green component of the pixel
    * @param b The blue component of the pixel
    * @return A number representing the certainty of the pixel being of the color
    */
  override protected def secondaryPixel(r: Int, g: Int, b: Int): Double = r + b
}

/**
  * Cyan color
  */
object Cyan extends SecondaryPixel {
  override def toString: String = "CYAN"

  /**
    * The cyan component
    *
    * @param r The red component of the pixel
    * @param g The green component of the pixel
    * @param b The blue component of the pixel
    * @return A number representing the certainty of the pixel being of the color
    */
  override protected def secondaryPixel(r: Int, g: Int, b: Int): Double = g + b
}

object LightBlue extends Color {
  override def toString: String = "LIGHT BLUE"

  val threshold = 256 * 3 / 2

  override def is(r: Int, g: Int, b: Int): Boolean = {
    Blue.is(r, g, b) && (r + g + b > threshold)
  }
}

object GreenYellow extends Color {
  override def toString: String = "GREEN YELLOW"

  override def is(r: Int, g: Int, b: Int): Boolean = Green.is(r, g, b) && Yellow.is(r, g, b)
}
