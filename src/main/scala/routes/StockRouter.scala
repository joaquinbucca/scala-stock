package routes

import java.util.UUID

import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.server.Directives._
import de.heikoseeberger.akkahttpcirce.CirceSupport
import model.entities.{StockEntity, StockMovement}
import model.services.StockService
import io.circe.generic.auto._
import io.circe.syntax._

import scala.concurrent.ExecutionContext

/**
  * Created by joaquinbucca on 9/27/16.
  */
class StockRouter(val productService: StockService)(implicit executionContext: ExecutionContext) extends CirceSupport {

  import productService._

  val route = pathPrefix("stocks") {
    pathEndOrSingleSlash {
      get {
        complete(getAllProductStocks.map(_.asJson))
      } ~
      post {
        entity(as[StockEntity]) { stock =>
          complete(createStockRow(stock).map(_.asJson))
        }
      }
    } ~
    pathPrefix(Rest) { productId =>
      pathEndOrSingleSlash {
        get {
          complete(getProductStockById(UUID.fromString(productId)).map(_.asJson))
        } ~
          delete {
            onSuccess(deleteProductStockRow(UUID.fromString(productId))) { ignored =>
              complete(NoContent)
            }
          }
      }
    } ~
    pathPrefix("add") {
      pathEndOrSingleSlash {
        post {
          entity(as[StockMovement]) { stock =>
            complete(addStock(stock).map(_.asJson))
          }
        }
      }
    } ~
    pathPrefix("remove") {
      pathEndOrSingleSlash {
        post {
          entity(as[StockMovement]) { stock =>
            complete(removeStock(stock).map(_.asJson))
          }
        }
      }
    } ~
    pathPrefix("reserve") {
      pathEndOrSingleSlash {
        post {
          entity(as[StockMovement]) { stock =>
            complete(reserveStock(stock).map(_.asJson))
          }
        }
      }
    } ~
    pathPrefix("removeReserve") {
      pathEndOrSingleSlash {
        post {
          entity(as[StockMovement]) { stock =>
            complete(removeReservedStock(stock).map(_.asJson))
          }
        }
      }
    } ~
    pathPrefix("free") {
      pathEndOrSingleSlash {
        post {
          entity(as[StockMovement]) { stock =>
            complete(unReserveStock(stock).map(_.asJson))
          }
        }
      }
    }
  }


}