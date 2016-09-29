package model.tables

import com.websudos.phantom.dsl.{ConsistencyLevel, _}
import model.entities.ProductEntity

import scala.concurrent.Future

/**
  * Created by joaquinbucca on 9/28/16.
  */
class Stock extends CassandraTable[ConcreteStock, ProductEntity] {

  object id extends StringColumn(this) with PartitionKey[String]
  object name extends StringColumn(this)
  object brand extends StringColumn(this)
  object price extends BigDecimalColumn(this)


  override def fromRow(r: Row): ProductEntity = ProductEntity(id(r), name(r), brand(r), price(r))
}



abstract class ConcreteStock extends Stock with RootConnector {

  def store(product: ProductEntity): Future[ResultSet] = {
    insert
      .value(_.id, product.id)
      .value(_.name, product.name)
      .value(_.brand, product.brand)
      .value(_.price, product.price)
      .consistencyLevel_=(ConsistencyLevel.ALL)
      .future()
  }

  def getAll: Future[Seq[ProductEntity]] = {
    select.limit(100).fetch()
  }

  def getById(productId: String): Future[Option[ProductEntity]] = {
    select.where(_.id eqs productId).one()
  }

  def deleteById(productId: String): Future[ResultSet] = {
    delete
      .where(_.id eqs productId)
      .consistencyLevel_=(ConsistencyLevel.ONE)
      .future()
  }
}