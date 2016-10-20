package routes

import akka.http.scaladsl.server._
import akka.http.scaladsl.server.directives.{BasicDirectives, FutureDirectives, HeaderDirectives, RouteDirectives}

/**
  * Created by joaquinbucca on 9/15/16.
  */
trait SecurityDirectives {

  import BasicDirectives._
  import FutureDirectives._
  import HeaderDirectives._
  import RouteDirectives._

//  def authenticate: Directive1[UserEntity] = {
//    headerValueByName("Token").flatMap { token =>
//      onSuccess(authService.authenticate(token)).flatMap {
//        case Some(user) => provide(user)
//        case None       => reject
//      }
//    }
//  }
//todo: ping external service , this is auth service

}


case class UserEntity(username: String, password: String) {
  require(!username.isEmpty, "username.empty")
  require(!password.isEmpty, "password.empty")
}

