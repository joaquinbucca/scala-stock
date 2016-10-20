package routes

import akka.http.scaladsl.server.Directives._
import model.services.StockService

import scala.concurrent.{ExecutionContext, Future}

/**
  * Created by joaquinbucca on 9/27/16.
  */
class RouteHandler(stockService: StockService)(implicit ex : ExecutionContext) {

  val stockRouter = new StockRouter(stockService)

  val routes = {
    logRequestResult("akka-http-microservice") {
      stockRouter.route ~
        pathPrefix("health") {
          pathEndOrSingleSlash {
            get {
              complete("true")
            }
          }
        }

    }
  }

}
