package uk.vitalcode

import uk.vitalcode.ColorMask.{Demand, Mask}

class PaintService {

  def optimizeBatch(colors: Int, demands: List[Demand]): String = {
    val masks: List[Mask] = demands.map(ColorMask(colors, _))
    val empty: Mask = List.fill[ColorType](colors)(Empty)

    def findPossibleSolutions(currentMask: Mask, restMasks: List[Mask]): List[Mask] = {
      restMasks match {
        case next :: rest =>
          ColorMask.findPermutations(currentMask, next, List(Nil)).flatMap(m => findPossibleSolutions(m, rest)).distinct
        case Nil =>
          currentMask :: Nil
      }
    }

    val possibleSolutions = if (masks.size > 1) findPossibleSolutions(masks.head, masks.tail) else masks

    val validSolutions = possibleSolutions.filter(solution => masks.forall(ColorMask.check(_, solution)))

    if (validSolutions.nonEmpty) {
      val minSolution = ColorMask.min(validSolutions: _*)
      ColorMask.show(minSolution)
    } else "IMPOSSIBLE"
  }
}
