package ru.hse.se.ba.danilin.paul.histogram

import ru.hse.se.ba.danilin.paul.histogram.atomizers.IAtomizer

case class Histogram[O](histogram: Map[O, Int])(implicit universe: ElementsUniverse[O])
  extends IHistogram[O] {

  override def apply(element: O): Int = histogram.getOrElse(element, 0)

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
    val histMap = atoms.foldLeft(Map.empty[O, Int])({
      case (acc, i) =>
        val newCount = acc.get(i) match {
          case None => 1
          case Some(prevCount) => prevCount + 1
        }
        acc + (i -> newCount)
    })
    Histogram(histMap)
  }
}
