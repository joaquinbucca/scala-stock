package utils

import java.net.{Inet4Address, InetAddress}

import com.typesafe.config.ConfigFactory
import consul.Consul
import consul.v1.common.Types

object ConsulHandler {

  val config = ConfigFactory.load()

  def register(sId : String, sType: String) = {
    // register service to consul

    val consulConfig = config.getConfig("consul")
    val consulAddress = consulConfig.getString("address")
    val consulPort = consulConfig.getInt("port")
    val httpPort = config.getInt("http.port")


    val consul = new Consul(new Inet4Address(consulAddress, consulPort))
    import consul.v1._

    val serviceId: Types.ServiceId = Types.ServiceId(sId)
    val stockType: Types.ServiceType = Types.ServiceType(sType)
    val address = InetAddress.getLocalHost.getHostAddress
    val myServiceCheck = agent.service.httpCheck(s"http://$address:$httpPort/health","15s")
    val myService = agent.service.LocalService(serviceId,ServiceType("micro"), Set(ServiceTag("prod")),Some(httpPort),Some(myServiceCheck))

    agent.service.register(myService)
  }
}
