package model.db

/**
  * Created by joaquinbucca on 9/20/16.
  */

import com.websudos.phantom.db.DatabaseImpl
import com.websudos.phantom.dsl._
import model.tables.ConcreteStock
import utils.DbConnector._

class StockDatabase(override val connector: KeySpaceDef) extends DatabaseImpl(connector) {
  object stockModel extends ConcreteStock with connector.Connector
}

/**
  * This is the production database, it connects to a secured cluster with multiple contact points
  */
object ProductionDb extends StockDatabase(connector)

trait ProductionDatabaseProvider {
  def database: StockDatabase
}

trait ProductionDatabase extends ProductionDatabaseProvider {
  override val database = ProductionDb
}

object ProductionDatabase extends ProductionDatabase with ProductionDatabaseProvider

/**
  * Thanks for the Phantom plugin, you can start an embedded cassandra in memory,
  * in this case we are using it for tests
  */
object EmbeddedDb extends StockDatabase(testConnector)

trait EmbeddedDatabaseProvider {
  def database: StockDatabase
}

trait EmbeddedDatabase extends EmbeddedDatabaseProvider {
  override val database = EmbeddedDb
}