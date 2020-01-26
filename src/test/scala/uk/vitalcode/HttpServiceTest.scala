package uk.vitalcode

import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.testkit.ScalatestRouteTest
import io.circe.generic.auto._
import io.circe.syntax._
import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.should.Matchers

import scala.annotation.tailrec
import scala.io.Source

class HttpServiceTest extends AnyFreeSpec with Matchers with ScalatestRouteTest with HttpService {

  val inputFile = Source.fromResource("unit-tests.in.txt").getLines.mkString(",").split(",").toList
  val requests = getOptimisationRequest(inputFile, Nil).reverse
  val outputs = Source.fromResource("unit-tests.out.txt").getLines.map("^.*: (.*)$".r.findAllIn(_).group(1)).toList

  @tailrec
  final def getOptimisationRequest(demands: List[String], agg: List[OptimisationRequest]): List[OptimisationRequest] = {
    demands match {
      case colours :: customers :: rest =>
        val customersCount = customers.toInt
        val demand = rest.take(customersCount).flatMap(_.split(" ")).map(_.toInt)
        getOptimisationRequest(rest.drop(customersCount), OptimisationRequest(colours.toInt, customers.toInt, demand) :: agg)
      case Nil => agg
    }
  }

  "HttpService acceptance test" - {
    "given input defined in input file" - {
      "should provide response specified in output file" in {
        requests.zip(outputs).foreach {
          case (in, out) => test(in, out)
        }

        def test(input: OptimisationRequest, output: String) {
          Get(s"/v1/?input=${input.asJson.noSpaces}") ~> routes ~> check {
            status shouldBe OK
            responseAs[String] shouldBe output
          }
        }
      }
    }
  }
}
