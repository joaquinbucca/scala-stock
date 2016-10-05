package model.entities

import com.websudos.phantom.dsl.UUID

/**
  * Created by joaquinbucca on 9/27/16.
  */

case class StockEntity(id: UUID, productId: UUID, available: Long, reserved: Long)


case class StockMovement(productId: UUID, quantity: Long)