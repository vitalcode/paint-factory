package uk.vitalcode

import uk.vitalcode.ColorMask.{Demand, Mask}

object PaintService {

  def optimizeBatch(colors: Int, demands: List[Demand]): String = {
    val masks: List[Mask] = demands.map(ColorMask(colors, _))
    val empty: Mask = List.fill[ColorType](colors)(Empty)

    def findPossibleSolutions(currentMask: Mask, restMasks: List[Mask]): List[Mask] = {
      restMasks match {
        case next :: rest =>
          val d = ColorMask.or(currentMask, next, List(Nil))
          val f = d.flatMap(m => findPossibleSolutions(m, rest))
          f
        case Nil =>
          currentMask :: Nil
      }
    }

    val possibleSolutions = findPossibleSolutions(empty, masks)

    val validSolutions = possibleSolutions.filter(solution => masks.forall(ColorMask.and(_, solution)))

    if (validSolutions.nonEmpty) {
      val minSolution = ColorMask.min(validSolutions: _*)
      ColorMask.show(minSolution)
    } else "IMPOSSIBLE"
  }
}
