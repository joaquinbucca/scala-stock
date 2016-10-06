package utils

import java.net.InetAddress

import com.typesafe.config.ConfigFactory
import com.websudos.phantom.connectors.{ContactPoint, ContactPoints}

import scala.collection.JavaConversions._

object DbConnector {

  val config = ConfigFactory.load()
  val cassandraConfig = config.getConfig("cassandra")

  val hosts = cassandraConfig.getStringList("host")
  val inets = hosts.map(InetAddress.getByName)

  val keySpace: String = cassandraConfig.getString("keyspace")

  /**
    * Create a connector with the ability to connects to
    * multiple hosts in a secured cluster
    */
  lazy val connector = ContactPoints(hosts).withClusterBuilder(
    _.withCredentials(
      cassandraConfig.getString("username"),
      cassandraConfig.getString("password")
    )
  ).keySpace(keySpace)

  /**
    * Create an embedded connector, used for testing purposes
    */
  lazy val testConnector = ContactPoint.embedded.noHeartbeat().keySpace("stock_test")
}


