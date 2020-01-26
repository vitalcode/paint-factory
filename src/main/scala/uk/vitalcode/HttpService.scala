package uk.vitalcode

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.HttpResponse
import akka.http.scaladsl.server.Directives._
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport
import io.circe.generic.auto._

case class OptimisationRequest(colors: Int, customers: Int, demands: List[List[Int]])

trait HttpService extends FailFastCirceSupport {
  def paintService: PaintService

  val routes = {
    pathPrefix("v1") {
      get {
        parameter("input".as[OptimisationRequest]) { input =>
          complete {
            HttpResponse(entity = paintService.optimizeBatch(input.colors, input.demands))
          }
        }
      }
    }
  }
}

object Main extends App with HttpService {
  implicit val system = ActorSystem()
  implicit val executor = system.dispatcher
  val paintService = new PaintService()

  val binding = Http().bindAndHandle(routes, "0.0.0.0", 8080)

  Runtime.getRuntime.addShutdownHook(
    new Thread(() => {
      binding.flatMap(_.unbind())
        .onComplete(_ => system.terminate())
    })
  )
}
