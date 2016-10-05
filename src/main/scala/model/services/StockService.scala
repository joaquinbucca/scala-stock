package model.services

import com.websudos.phantom.dsl.UUID
import model.entities.{StockEntity, StockMovement}

import scala.concurrent.{ExecutionContext, Future}

/**
  * Created by joaquinbucca on 9/27/16.
  */
class StockService(implicit executionContext: ExecutionContext) {

  import model.db.ProductionDatabase.database._

  def getAllProductStocks: Future[Seq[StockEntity]] = stockModel.getAll

  def getProductStockById(productId: UUID): Future[Option[StockEntity]] = stockModel.getByProductId(productId)

  def createStockRow(stock: StockEntity): Future[StockEntity] = {
    stockModel.store(stock).flatMap(p =>  stockModel.getById(stock.id).map(f => f.get ))
  }

  def addStock(move: StockMovement): Future[StockEntity] = {
    stockModel.store(StockEntity(move.productId, move.productId, 0, 0)).flatMap(p =>  stockModel.getByProductId(move.productId).map(f => f.get))
  }

  def removeStock(move: StockMovement): Future[StockEntity] = {
    stockModel.decrementAvailable(move).flatMap(p =>  stockModel.getByProductId(move.productId).map(f => f.get))
  }

  def reserveStock(move: StockMovement): Future[StockEntity] = {
    stockModel.incrementReserved(move).flatMap(p =>  stockModel.getByProductId(move.productId).map(f => f.get))
  }

  def removeReservedStock(move: StockMovement): Future[StockEntity] = {
    stockModel.decrementReserved(move).flatMap(p =>  stockModel.getByProductId(move.productId).map(f => f.get))
  }

  def unReserveStock(move: StockMovement): Future[StockEntity] = {
    stockModel.unReserved(move).flatMap(p =>  stockModel.getByProductId(move.productId).map(f => f.get))
  }

  def deleteProductStockRow(productId: UUID): Future[UUID] = stockModel.deleteByProductId(productId).map(rs => productId)


}
