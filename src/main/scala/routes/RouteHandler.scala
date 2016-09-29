package routes

import akka.http.scaladsl.server.Directives._

import scala.concurrent.ExecutionContext

/**
  * Created by joaquinbucca on 9/27/16.
  */
class RouteHandler(productsService: ProductService)(implicit ex : ExecutionContext) {

  val productRouter = new StockRouter(productsService)

  val routes = {
    logRequestResult("akka-http-microservice") {
      productRouter.route

    }
  }

}
