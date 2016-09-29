package routes

import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.server.Directives._

import scala.concurrent.ExecutionContext

/**
  * Created by joaquinbucca on 9/27/16.
  */
class StockRouter(val productService: ProductService)(implicit executionContext: ExecutionContext) extends CirceSupport {

  val route = pathPrefix("products") {
    pathEndOrSingleSlash {
      get {
        complete(getAllProducts.map(_.asJson))
      } ~
      post {
        entity(as[ProductEntity]) { product =>
          complete(createProduct(product).map(_.asJson))
        }
      }
    } ~
    pathPrefix(Rest) { productId =>
      pathEndOrSingleSlash {
        get {
          complete(getProductById(productId).map(_.asJson))
        } ~
          post {
            entity(as[ProductEntity]) { productUpdate =>
              complete(updateProduct(productUpdate).map(_.asJson))
            }
          } ~
          delete {
            onSuccess(deleteProduct(productId)) { ignored =>
              complete(NoContent)
            }
          }
      }
    }
  }


}