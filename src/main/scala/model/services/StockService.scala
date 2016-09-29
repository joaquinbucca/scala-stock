package model.services

import model.entities.ProductEntity

import scala.concurrent.{ExecutionContext, Future}

/**
  * Created by joaquinbucca on 9/27/16.
  */
class StockService(implicit executionContext: ExecutionContext) {

  import model.db.ProductionDatabase.database._

  def getAllProducts: Future[Seq[ProductEntity]] = stockModel.getAll

  def getProductById(productId: String): Future[Option[ProductEntity]] = stockModel.getById(productId)

  def createProduct(product: ProductEntity): Future[ProductEntity] = {
    stockModel.store(product).flatMap(p =>  stockModel.getById(product.id).map(f => f.get ))
  }

  def updateProduct(productUpdate: ProductEntity): Future[ProductEntity] = {
    stockModel.store(productUpdate).flatMap(p =>  stockModel.getById(productUpdate.id).map(f => f.get))
  }

  def deleteProduct(productId: String): Future[String] = stockModel.deleteById(productId).map(rs => productId)


}
