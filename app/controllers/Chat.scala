package controllers

import chats.actors.UserActor
import play.api.mvc._
import helpers._
import play.api.libs.json._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import play.api.Play.current

object Chat extends Controller {

  def chatRoomJs = Action { implicit request =>
    Ok(views.js.chat(request))
  }

  def chat = WebSocket.tryAcceptWithActor[JsValue, JsValue] { implicit request  =>
    Future.successful(Cfg.user(request.session) match {
      case None => Left(Forbidden)
      case Some(user) => Right(UserActor.props(user))
    })
  }

}
