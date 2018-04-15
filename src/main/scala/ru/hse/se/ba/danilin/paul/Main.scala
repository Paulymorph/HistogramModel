package ru.hse.se.ba.danilin.paul

import java.awt.image.BufferedImage
import java.io.{ByteArrayOutputStream, File}

import javax.imageio.ImageIO.{read => readImage, write => writeImage}
import org.json4s.native.Serialization.{read => readJson}
import ru.hse.se.ba.danilin.paul.histogram_model.atomizers._
import ru.hse.se.ba.danilin.paul.histogram_model.histogram.Histogram
import ru.hse.se.ba.danilin.paul.histogram_model.histogram.Implicits._
import ru.hse.se.ba.danilin.paul.histogram_model.queries.{HistogramInput, Input, Query, SubhistogramInput}

import scala.collection.mutable

object Main {

  case class HistogramItem(histogram: Histogram[Color], name: String, image: Array[Byte]) {
    def input = HistogramInput(histogram)

    def query(queryString: String)(implicit aliases: scala.collection.Map[String, Input[Color]]) = {
      val q = Query.fromString(queryString)(aliases)
      val source = histogram
      q.execute(source)
    }

    def similar(queryString: String)(implicit aliases: scala.collection.Map[String, Input[Color]]) = {
      val res = query(queryString)(aliases).left.get
      res similar histogram
    }
  }

  def imageToBytes(image: BufferedImage): Array[Byte] = {
    val baos = new ByteArrayOutputStream()
    writeImage(image, "png", baos)
    baos.flush()
    baos.toByteArray()
  }

  def readImages(folder: File) = {
    val files = (folder.isDirectory match {
      case true => folder.listFiles().toList
      case false => List(folder)
    }).filterNot(_.getName.startsWith("."))

    for {
      file <- files
      name = file.getName.split('.')(0)
      bufferedImage = readImage(file)
      histogram = bufferedImage.toHistogram.normalize
      imageBytes = imageToBytes(bufferedImage)
    } yield HistogramItem(histogram, name, imageBytes)
  }

  def main(args: Array[String]): Unit = {
    val histsList = readImages(new File("/Users/pauldanilin/Documents/HSE/CourseWorks/HistogramModel/Demonstration/images"))
    val hists = histsList.map(_.name).zip(histsList).toMap

    val imagesAliases = hists.mapValues(_.input)

    val pixelsAliases = Map(
      "blue" -> SubhistogramInput(Set(Blue, LightBlue)),
      "red" -> SubhistogramInput(Set(Red)),
      "green" -> SubhistogramInput(Set(GreenYellow, Green)),
      "yellow" -> SubhistogramInput(Set(GreenYellow, Yellow)),
      "greenyellow" -> SubhistogramInput(Set(GreenYellow)),
      "cyan" -> SubhistogramInput(Set(Cyan)),
      "magenta" -> SubhistogramInput(Set(Magenta)))

    import ru.hse.se.ba.danilin.paul.histogram_model.queries.Input

    implicit val aliases = mutable.Map((Query.standardAliases[Color] ++ imagesAliases ++ pixelsAliases).asInstanceOf[Map[String, Input[Color]]].toSeq: _*)


    val q = Query.fromString("green-greenyellow")
    val sea = hists("sea").histogram
    q.execute(sea)



//    val h = HistogramImpl.extract("d f")
//    val ser = write(r)
//    val out = readJson[HistogramImpl[Color]](ser)
//    println(ser)
  }
}

