package uk.vitalcode

import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.testkit.ScalatestRouteTest
import io.circe.generic.auto._
import io.circe.syntax._
import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.should.Matchers

import scala.annotation.tailrec
import scala.io.Source

class HttpServiceTest extends AnyFreeSpec with Matchers with ScalatestRouteTest {

  val inputFile = Source.fromResource("unit-tests.in.txt").getLines.mkString(",").split(",").toList
  val requests = formatOptimisationRequest(inputFile, Nil).reverse
  val outputs = Source.fromResource("unit-tests.out.txt").getLines.map("^.*: (.*)$".r.findAllIn(_).group(1)).toList
  val httpService = new HttpService {
    override def paintService = new PaintService()
  }

  @tailrec
  final def formatOptimisationRequest(demands: List[String], agg: List[OptimisationRequest]): List[OptimisationRequest] = {
    demands match {
      case colours :: customers :: rest =>
        val customersCount = customers.toInt
        val demand = rest.take(customersCount).map(_.split(" ").map(_.toInt).toList)
        formatOptimisationRequest(rest.drop(customersCount), OptimisationRequest(colours.toInt, customers.toInt, demand) :: agg)
      case _ => agg
    }
  }

  "HttpService acceptance test" - {
    "given input defined in input file" - {
      "should provide response specified in output file" in {
        requests.zip(outputs).foreach {
          case (input, output) =>
            Get(s"/v1/?input=${input.asJson.noSpaces}") ~> httpService.routes ~> check {
              status shouldBe OK
              responseAs[String] shouldBe output
            }
        }
      }
    }
  }
}
