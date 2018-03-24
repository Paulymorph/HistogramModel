package ru.hse.se.ba.danilin.paul.histogram

import ru.hse.se.ba.danilin.paul.histogram.atomizers.IAtomizer

case class Histogram[O](histogram: Map[O, Double])(implicit universe: ElementsUniverse[O])
  extends IHistogram[O] {

  override def apply(element: O): Double = histogram.getOrElse(element, 0)

  override def elementsUniverse: ElementsUniverse[O] = universe

  override def elementsPresent: Set[O] = histogram.keySet

  override def subHistogram(newElementsUniverse: ElementsUniverse[O]): IHistogram[O] = {
    val filteredHistogram = histogram.filterKeys(element => newElementsUniverse.isElementInUniverse(element))
    Histogram(filteredHistogram)(newElementsUniverse)
  }
}

object Histogram {
  def extract[S, O](source: S)(implicit atomizer: IAtomizer[S, O]): IHistogram[O] = {
    val atoms = atomizer atomize source
    val histMap = atoms.foldLeft(Map.empty[O, Double])({
      case (elementsMap, i) =>
        val newCount = elementsMap.get(i).fold(1.0)(_ + 1)
        elementsMap + (i -> newCount)
    })
    Histogram(histMap)
  }
}


