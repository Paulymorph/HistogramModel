package ru.hse.se.ba.danilin.paul.histogram

import ru.hse.se.ba.danilin.paul.histogram.atomizers.{IAtomizer, IElementsUniverse}

case class Histogram[S, O](histogram: Map[O, Int])(implicit universe: IElementsUniverse[O])
  extends IHistogram[O] {

  override def apply(element: O): Int = histogram.getOrElse(element, 0)

  override def elementsUniverse: Set[O] = universe.elementsUniverse

  override def elementsPresent: Set[O] = histogram.keySet

  override def subHistogram(elements: Set[O]): IHistogram[O] = {
    val filteredHistogram = histogram.filterKeys(element => elements.contains(element))
    Histogram(filteredHistogram)
  }

  override def similarity(another: IHistogram[O]): Double = ???
}

object Histogram {
  def extract[S, O](source: S)(implicit atomizer: IAtomizer[S, O]) = {
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
