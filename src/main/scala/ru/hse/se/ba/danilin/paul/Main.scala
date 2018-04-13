package ru.hse.se.ba.danilin.paul

import java.io.File

import javax.imageio.ImageIO.{read => readImage}
import org.json4s.native.Serialization.{read => readJson}
import ru.hse.se.ba.danilin.paul.histogram_model.atomizers._
import ru.hse.se.ba.danilin.paul.histogram_model.histogram.Implicits._
import ru.hse.se.ba.danilin.paul.histogram_model.queries.{HistogramInput, Input, Query, SubhistogramInput}

object Main {

  def readImages(folder: File) = {
    val files = (folder.isDirectory match {
      case true => folder.listFiles().toList
      case false => List(folder)
    }).filterNot(_.getName.startsWith("."))
    val names = files.map(_.getName.split('.')(0))
    val hists = files
      .filterNot(_.isHidden)
      .map(f => readImage(f))
      .map(_.toHistogram)
    names.zip(hists)
  }
  def main(args: Array[String]): Unit = {
    val imagesHists = readImages(new File("/Users/pauldanilin/Documents/HSE/CourseWorks/HistogramModel/Demonstration/images"))
      .toMap.mapValues(_.normalize)
    val imagesAliases = imagesHists.mapValues( hist => HistogramInput(hist))
    val pixelsAliases = Map("blue" -> SubhistogramInput(Set(Blue)),
      "red" -> SubhistogramInput(Set(Red, Yellow)),
      "green" -> SubhistogramInput(Set(Green)),
      "yellow" -> SubhistogramInput(Set(Yellow)),
      "cyan" -> SubhistogramInput(Set(Cyan)),
      "magenta" -> SubhistogramInput(Set(Magenta)))

    implicit val aliases = (Query.standardAliases[Color] ++ imagesAliases ++ pixelsAliases).asInstanceOf[Map[String, Input[Color]]]
    val a = Query.fromString("red|yellow")(aliases)
    val r = a.execute(imagesHists("tiger"))



//    val h = HistogramImpl.extract("d f")
//    val ser = write(r)
//    val out = readJson[HistogramImpl[Color]](ser)
//    println(ser)
  }
}

