package uk.vitalcode

import scala.annotation.tailrec
import scala.collection.mutable.ArrayBuffer

object ColorMask {

  type Mask = List[ColorType]
  type Demand = List[Int]

  def apply(colors: Int, demand: Demand): Mask = {
    val mask = ArrayBuffer.fill[ColorType](colors)(Empty)
    demand.tail.grouped(2).foreach {
      case color :: colorType :: Nil => mask.update(color - 1, if (colorType == 1) Gloss else Matte)
    }
    mask.toList
  }

  def or(m1: Mask, m2: Mask, agg: List[Mask]): List[Mask] = (m1, m2, agg) match {
    case (p1 :: rest1, p2 :: rest2, aggH :: aggT) if p1 == p2 =>
      or(rest1, rest2, (p1 :: aggH) :: aggT)
    case (p1 :: rest1, p2 :: rest2, aggH :: aggT) =>
      or(rest1, rest2, (p1 :: aggH) :: aggT) ::: or(rest1, rest2, (p2 :: aggH) :: aggT)
    case _ => agg.map(_.reverse).reverse
  }

  @tailrec
  def and(m1: Mask, m2: Mask): Boolean = (m1, m2) match {
    case (p1 :: _, p2 :: _) if p1 == p2 && p1 != Empty => true
    case (_ :: rest1, _ :: rest2) => and(rest1, rest2)
    case _ => false
  }

  def min(masks: Mask*): Mask = masks.map(mask => toBinaryMask(mask).sum -> mask).minBy(_._1)._2

  def show(mask: Mask): String = toBinaryMask(mask).mkString(" ")

  private def toBinaryMask(mask: Mask): List[Int] = mask.map(color => if (color == Gloss) 1 else 0)
}

