package pegler
package app

import argonaut._, Argonaut._

case class Api(sprites: SpriteStore) extends api.Api

case class ApiWithUploads(sprites: SpriteStore) {
  val rd = Api(sprites)

  import json.encode._
  import unfiltered.request._
  import unfiltered.response._
  import unfiltered.filter.request._
  import unfiltered.directives._, Directives._
  import javax.servlet.http.HttpServletRequest

  def multipart = when { case MultiPart(_) => } orElse BadRequest

  def intent[A, B] = rd.intent orElse (Directive.Intent.Path {
    case Seg(List("sprites", name)) =>
    for {
      _ <- POST
      owner <- api.Owner.identity
      _ <- multipart
      r <- request[HttpServletRequest]
    } yield {
      MultiPartParams.Memory(r).files("image") match {
        case Seq(file, _*) if file.contentType.startsWith("image") =>
        val model = sprites.save(owner, file.name, file.bytes)
        Created ~>
        JsonContent ~>
        ResponseString(model.asJson.toString())
        case _ =>
        BadRequest ~>
        ResponseString("The file must be a small image.")
      }
    }
  })
}
