package utils

import com.typesafe.config.ConfigFactory

/**
  * Created by joaquinbucca on 9/27/16.
  */
trait Config {

  private val config = ConfigFactory.load()
  private val httpConfig = config.getConfig("http")

  val httpHost = httpConfig.getString("interface")
  val httpPort = httpConfig.getInt("port")

  private val consulConfig = config.getConfig("consul")
  val consulAddress = consulConfig.getString("address")
  val consulPort = consulConfig.getInt("port")

}
