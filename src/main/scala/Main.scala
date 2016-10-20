import akka.actor.ActorSystem
import akka.event.Logging
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import com.websudos.phantom.dsl.KeySpaceDef
import model.db.ProductionDb
import model.services.StockService
import routes.RouteHandler
import utils.{Config, ConsulHandler}

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.language.postfixOps


/**
  * Created by joaquinbucca on 9/27/16.
  */
object Main extends App with Config {

  implicit val system = ActorSystem()
  implicit val executor = system.dispatcher
  implicit val materializer = ActorMaterializer()


  val logger = Logging(system, getClass)


  private val stockService: StockService = new StockService
  val routeHandler = new RouteHandler(stockService)

  private val connector: KeySpaceDef = ProductionDb.connector

  implicit val keySpace = connector.provider.space
  implicit val session = connector.session

  Await.result(ProductionDb.autocreate.future(), 10 seconds) //todo : awaits are not recommended in production

  ConsulHandler.register("stock", "STOCK")

  Http().bindAndHandle(routeHandler.routes, httpHost, httpPort)

}