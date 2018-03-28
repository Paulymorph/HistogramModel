package ru.hse.se.ba.danilin.paul.histogram_model.histogram

import ru.hse.se.ba.danilin.paul.histogram_model.atomizers.Atomizer

case class HistogramImpl[O](histogram: Map[O, Double])(implicit universe: ElementsUniverse[O])
  extends Histogram[O] {

  override def apply(element: O): Double = histogram.getOrElse(element, 0)

  override def elementsUniverse: ElementsUniverse[O] = universe

  override def elementsPresent: Set[O] = histogram.keySet

  override def subHistogram(newElementsUniverse: ElementsUniverse[O]): Histogram[O] = {
    val filteredHistogram = histogram.filterKeys(element => newElementsUniverse.isElementInUniverse(element))
    HistogramImpl(filteredHistogram)(newElementsUniverse)
  }
}

object HistogramImpl {
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


