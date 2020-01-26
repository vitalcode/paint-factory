package uk.vitalcode

import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.should.Matchers

class PaintServiceTest extends AnyFreeSpec with Matchers {

  "optimizeBatch" - {
    "should be impossible" in {
      val demand = List(1, 1, 0) :: List(1, 1, 1) :: Nil
      PaintService.optimizeBatch(1, demand) shouldBe "IMPOSSIBLE"
    }

    "should no matte" in {
      val demand = List(1, 1, 0) :: List(1, 2, 0) :: Nil
      PaintService.optimizeBatch(2, demand) shouldBe "0 0"
    }

    "should all matte" in {
      val demand = List(1, 1, 1) :: List(2, 1, 0, 2, 1) :: List(3, 1, 0, 2, 0, 3, 1) :: Nil
      PaintService.optimizeBatch(3, demand) shouldBe "1 1 1"
    }

    "should color_not_requested" in {
      val demand = List(1, 5, 1) :: List(2, 1, 0, 2, 1) :: Nil
      PaintService.optimizeBatch(5, demand) shouldBe "0 0 0 0 1"
    }
  }
}
