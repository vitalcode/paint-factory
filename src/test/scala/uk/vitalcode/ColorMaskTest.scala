package uk.vitalcode

import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.should.Matchers
import uk.vitalcode.ColorMask.Mask

class ColorMaskTest extends AnyFreeSpec with Matchers {

  "apply" - {
    "should create instance of ColorMask" in {
      ColorMask(2, List(1,1,0)) shouldBe Matte :: Empty :: Nil
      ColorMask(2, List(1,2,1)) shouldBe Empty :: Gloss :: Nil
      ColorMask(2, List(2,1,0,2,1)) shouldBe Matte :: Gloss :: Nil
    }
  }

  "findPermutations" - {
    "should return all possible permutations for two masks" in {
      val m1: Mask = Matte :: Empty :: Nil
      val m2: Mask = Empty :: Matte :: Nil

      val e1: Mask = Matte :: Empty :: Nil
      val e2: Mask = Matte :: Matte :: Nil
      val e3: Mask = Empty :: Empty :: Nil
      val e4: Mask = Empty :: Matte :: Nil

      ColorMask.findPermutations(m1, m2, List(Nil)) shouldBe e1 :: e2 :: e3 :: e4 :: Nil
    }
  }

  "check" - {
    val m1: Mask = Gloss :: Empty :: Empty :: Empty :: Empty :: Nil
    val m2: Mask = Matte :: Matte :: Empty :: Empty :: Empty :: Nil

    val s1: Mask = Gloss :: Matte :: Empty :: Empty :: Empty :: Nil
    val s2: Mask = Matte :: Matte :: Empty :: Empty :: Empty :: Nil

    "should return true if two masks are matched" in {
      ColorMask.check(m1, s1) shouldBe true
      ColorMask.check(m2, s1) shouldBe true
      ColorMask.check(m2, s2) shouldBe true
    }
    "should return false if two masks cannot be each other solutions" in {
      ColorMask.check(m1, s2) shouldBe false
    }
  }

  "min" - {
    "should prioritise solutions with minimum gloss colors" in {
      val s1: Mask = Gloss :: Matte :: Gloss :: Empty :: Matte :: Nil
      val s2: Mask = Matte :: Matte :: Matte :: Empty :: Empty :: Nil
      val s3: Mask = Matte :: Matte :: Gloss :: Empty :: Matte :: Nil

      ColorMask.min(s1, s2, s3) shouldBe s2
    }
  }

  "show" - {
    "should return color mask string representation" in {
      val s1: Mask = Matte :: Matte :: Gloss :: Empty :: Matte :: Nil

      ColorMask.show(s1) shouldBe "0 0 1 0 0"
    }
  }
}

