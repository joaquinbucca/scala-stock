package model.tables

import com.websudos.phantom.dsl.{ConsistencyLevel, _}
import model.entities.StockEntity
import model.entities.StockMovement

import scala.concurrent.Future

/**
  * Created by joaquinbucca on 9/28/16.
  */
class Stock extends CassandraTable[ConcreteStock, StockEntity] {

  object id extends UUIDColumn(this) with PartitionKey[UUID]
  object productId extends UUIDColumn(this) with PrimaryKey[UUID]
  object available extends CounterColumn(this)
  object reserved extends CounterColumn(this)


  override def fromRow(r: Row): StockEntity = StockEntity(id(r), productId(r), available(r), reserved(r))
}



abstract class ConcreteStock extends Stock with RootConnector {

  def store(stock: StockEntity): Future[ResultSet] = {
    insert
      .value(_.id, stock.id)
      .value(_.productId, stock.productId)
      .value(_.available, stock.available)
      .value(_.reserved, stock.reserved)
      .consistencyLevel_=(ConsistencyLevel.ALL)
      .future()
  }

  def getAll: Future[Seq[StockEntity]] = {
    select.limit(100).fetch()
  }

  def getById(stockId: UUID): Future[Option[StockEntity]] = {
    select.where(_.id eqs stockId).one()
  }

  def getByProductId(productId: UUID): Future[Option[StockEntity]] = {
    select.where(_.productId eqs productId).one()
  }

  def deleteById(stockId: UUID): Future[ResultSet] = {
    delete
      .where(_.id eqs stockId)
      .consistencyLevel_=(ConsistencyLevel.ONE)
      .future()
  }

  def deleteByProductId(productId: UUID): Future[ResultSet] = {
    delete
      .where(_.productId eqs productId)
      .consistencyLevel_=(ConsistencyLevel.ONE)
      .future()
  }

  def incrementAvailable(move: StockMovement): Future[ResultSet] = {
    update
      .where(_.productId eqs move.productId)
      .modify(_.available += move.quantity)
      .future()
  }

  def decrementAvailable(move: StockMovement): Future[ResultSet] = {
    update
      .where(_.productId eqs move.productId)
      .modify(_.available -= move.quantity)
      .future()
  }

  def incrementReserved(move: StockMovement): Future[ResultSet] = {
    update
      .where(_.productId eqs move.productId)
//      .modify(_.available -= move.quantity)
      .modify(_.reserved += move.quantity)
      .future()
  }

  def decrementReserved(move: StockMovement): Future[ResultSet] = {
    update
      .where(_.productId eqs move.productId)
      .modify(_.reserved -= move.quantity)
      .future()
  }

  def unReserved(move: StockMovement): Future[ResultSet] = {
    update
      .where(_.productId eqs move.productId)
//      .modify(_.available += move.quantity)
      .modify(_.reserved -= move.quantity)
      .future()
  }
}