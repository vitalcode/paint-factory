package uk.vitalcode

import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.should.Matchers
import uk.vitalcode.ColorMask.Mask

class ColorMaskTest extends AnyFreeSpec with Matchers {

  "apply" - {
    "should" in {

      /*
        def test_no_matte(self):
          demand = [[1, 1, 0], [1, 2, 0]]
          self.assertEqual(convert_and_call(2, 2, demand), "0 0")
       */

      ColorMask(2, List(1,1,0)) shouldBe Matte :: Empty :: Nil
      ColorMask(2, List(1,2,1)) shouldBe Empty :: Gloss :: Nil
      ColorMask(2, List(2,1,0,2,1)) shouldBe Matte :: Gloss :: Nil
    }
  }

  "or" - {
    "should" in {

      val m1: Mask = Matte :: Empty :: Nil
      val m2: Mask = Empty :: Matte :: Nil

      val e1: Mask = Matte :: Empty :: Nil
      val e2: Mask = Matte :: Matte :: Nil
      val e3: Mask = Empty :: Empty :: Nil
      val e4: Mask = Empty :: Matte :: Nil

      ColorMask.or(m1, m2, List(Nil)) shouldBe e1 :: e2 :: e3 :: e4 :: Nil
    }
  }

  "and" - {
    "should" in {

      /*
        CM1   - g 0 0 0 0
        CM2   - m m 0 0 0

        Sol1  - g m g 0 m - satisfies CM1 and CM2
        Sol2  - m m g 0 m
       */

      val m1: Mask = Gloss :: Empty :: Empty :: Empty :: Empty :: Nil
      val m2: Mask = Matte :: Matte :: Empty :: Empty :: Empty :: Nil

      val s1: Mask = Gloss :: Matte :: Empty :: Empty :: Empty :: Nil
      val s2: Mask = Matte :: Matte :: Empty :: Empty :: Empty :: Nil

      ColorMask.and(m1, s1) shouldBe true
      ColorMask.and(m2, s1) shouldBe true
      ColorMask.and(m1, s2) shouldBe false
      ColorMask.and(m2, s2) shouldBe true
    }
  }

  "min" - {
    "should" in {

      /*
        Sol1  - g m g 0 m
        Sol2  - m m m 0 0
        Sol3  - m m g 0 m
       */

      val s1: Mask = Gloss :: Matte :: Gloss :: Empty :: Matte :: Nil
      val s2: Mask = Matte :: Matte :: Matte :: Empty :: Empty :: Nil
      val s3: Mask = Matte :: Matte :: Gloss :: Empty :: Matte :: Nil


      ColorMask.min(s1, s2, s3) shouldBe s2
    }
  }

  "show" - {
    "should" in {
      val s1: Mask = Matte :: Matte :: Gloss :: Empty :: Matte :: Nil

      ColorMask.show(s1) shouldBe "0 0 1 0 0"
    }
  }
}

