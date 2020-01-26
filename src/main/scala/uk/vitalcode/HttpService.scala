package uk.vitalcode

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.stream.{ActorMaterializer, Materializer}
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport
import io.circe.generic.auto._
import uk.vitalcode.ColorMask.Demand

import scala.concurrent.ExecutionContextExecutor

case class OptimisationRequest(colors: Int, customers: Int, demands: List[Int])

trait HttpService extends FailFastCirceSupport {
  implicit val system: ActorSystem

  implicit def executor: ExecutionContextExecutor

  implicit val materializer: Materializer

  def getCustomerDemands(demands: Demand, agg: List[Demand]): List[Demand] = { // TODO refactor out
    demands match {
      case h :: rest => getCustomerDemands(rest.drop(h * 2), (h :: rest.take(h * 2)) :: agg)
      case Nil => agg
    }
  }

  val routes = {
    pathPrefix("v1") {
      get {
        parameter("input".as[OptimisationRequest]) { input =>
          complete {
            val customerDemands = getCustomerDemands(input.demands, Nil)
            PaintService.optimizeBatch(input.colors, customerDemands) // TODO inject
          }
        }
      }
    }
  }
}

object App extends App with HttpService {
  override implicit val system = ActorSystem()
  override implicit val executor = system.dispatcher
  override implicit val materializer = ActorMaterializer()

  val port = 8080
  val interface = "0.0.0.0"

  Http().bindAndHandle(routes, interface, port)
}
