import java.net.{Inet4Address, InetAddress}

import akka.actor.ActorSystem
import akka.event.Logging
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import com.websudos.phantom.dsl.KeySpaceDef
import consul.Consul
import consul.v1.catalog.{Check, Registerable, Service}
import consul.v1.common.{CheckStatus, Types}
import model.db.ProductionDb
import model.services.StockService
import routes.RouteHandler
import utils.Config

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

  //todo register service to consul

  val consul = new Consul(new Inet4Address(consulAddress, consulPort))
  import consul.v1._
  val nodeId = Types.NodeId("stock1") //todo: something identifyng this node, maybe the ip address?

  //todo: right now, im using catalog to register nodes and services, should i use agent instead? is agent local and automatically updates to cluster or what?
  // register via agent and query via dns queries or catalog.

  private val serviceId: Types.ServiceId = Types.ServiceId("stock")

  val service = Service(serviceId, Types.ServiceType("micro"),
    Set(), Option(InetAddress.getLocalHost.getHostAddress), Option(httpPort))

  val check = Check(nodeId, Types.CheckId("stockCheck"), "stockCheck", None, CheckStatus.passing, Option(serviceId))

  catalog.register(Registerable(nodeId, consulAddress, Option(service), Option(check), Option(Types.DatacenterId("blabla"))))

  Http().bindAndHandle(routeHandler.routes, httpHost, httpPort)

}