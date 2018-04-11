package ru.hse.se.ba.danilin.paul.histogram_model.histogram

import ru.hse.se.ba.danilin.paul.histogram_model.atomizers.Atomizer

/**
  * An implementation of the histogram
  * @param histogram The histogram contents
  * @param universe The universe of the histogram
  * @tparam O The elements type of the histogram
  */
case class HistogramImpl[O](histogram: Map[O, Double])(implicit universe: ElementsUniverse[O])
  extends Histogram[O] {

  /**
    * The presence of the element
    * @param element The element of a histogram
    * @return The presence of the element
    */
  override def apply(element: O): Double = histogram.getOrElse(element, 0)

  /**
    * The elements universe of the histogram
    * @return The elements universe of the histogram
    */
  override def elementsUniverse: ElementsUniverse[O] = universe

  /**
    * The set of elements present in the histogram
    * @return The set of elements present in the histogram
    */
  override def elementsPresent: Set[O] = histogram.keySet

  /**
    * A subhistogram of the histogram
    * @param newElementsUniverse A new universe of the histogram
    * @return A subhistogram of the histogram
    */
  override def subHistogram(newElementsUniverse: ElementsUniverse[O]): Histogram[O] = {
    val filteredHistogram = histogram.filterKeys(element => newElementsUniverse.isElementInUniverse(element))
    HistogramImpl(filteredHistogram)(newElementsUniverse)
  }

  /**
    * The normalized histogram
    * @return The normalized histogram
    */
  override def normalize(): Histogram[O] = {
    val n = histogram.values.sum
    HistogramImpl(histogram.mapValues(_ / n))
  }
}

object HistogramImpl {
  /**
    * Extracts the histogram from an object
    * @param source The source object to extract histogram from
    * @param atomizer The atomizer for the source type
    * @tparam S The type of the source object
    * @tparam O The type of the elements of the histogram
    * @return The extracted histogram from the source
    */
  def extract[S, O](source: S)(implicit atomizer: Atomizer[S, O]): Histogram[O] = {
    val atoms = atomizer atomize source
    val histMap = atoms.foldLeft(Map.empty[O, Double])({
      case (elementsMap, i) =>
        val newCount = elementsMap.get(i).fold(1.0)(_ + 1)
        elementsMap + (i -> newCount)
    })
    HistogramImpl(histMap)
  }
}


