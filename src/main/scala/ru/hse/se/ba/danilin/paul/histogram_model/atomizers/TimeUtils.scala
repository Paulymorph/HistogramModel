package ru.hse.se.ba.danilin.paul.histogram_model.atomizers

object TimeUtils {
  def time[T](block: => T, name: Option[String] = None): T = {
    val start = System.nanoTime()
    val res = block
    val end = System.nanoTime()

    val duration = f"${(end - start) / 1e9}%.2f seconds"

    val message = name match {
      case None => s"Evaluated for $duration"
      case Some(name) => s"$name evaluated $duration"
    }

    println(message)
    res
  }
}
