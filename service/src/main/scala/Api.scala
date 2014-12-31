package pegler
package api

import argonaut._, Argonaut._
import unfiltered.request._
import unfiltered.response._
import unfiltered.directives._, Directives._

object Owner {
  def identity = headers("X-Authorized-Identity")
    .map({
      case headers if headers.hasNext => headers.next
    })
    .orElse(
      failure(new ResponseJoiner("Invalid identity")(message =>
        Unauthorized ~> ResponseString(message.mkString("\n"))
      ))
    )
}

trait Api {
  import json.encode._

  val sprites: SpriteStore

  def intent[A, B] = Directive.Intent.Path {
    case Seg(List("sprites")) =>
    for {
      _ <- GET
      owner <- Owner.identity
    } yield {
      Ok ~>
      JsonContent ~>
      ResponseString(sprites.readOwner(owner).asJson.toString())
    }
    case Seg(List("sprites", name)) =>
    val get = for {
      _ <- GET
      owner <- Owner.identity
    } yield {
      sprites.readByName(owner, name).map {
        case model =>
        Ok ~>
        JsonContent ~>
        ResponseString(model.sprite.asJson.toString())
      }.orElse {
        Some(NotFound ~> ResponseString(s"Sprite with id ${guid} was not found"))
      }.get
    }
    val delete = for {
      _ <- DELETE
      owner <- Owner.identity
    } yield {
      sprites.deleteByName(owner, name)
      NoContent
    }
    get | delete
  }
}
