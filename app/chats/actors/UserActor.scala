package chats.actors

import akka.actor._
import akka.event.LoggingReceive
import helpers.Cfg
import models.User
import play.api.libs.json.{JsValue, Json}

import scala.xml.Utility

class UserActor(user: User, room: ActorRef, out: ActorRef) extends Actor with ActorLogging {

  override def preStart() = {
    RoomActor() ! Join(user)
  }

  override def postStop() = {
    RoomActor() ! Quit(user)
  }

  def receive = LoggingReceive {
      case Message(user, kind, text, online) => {
        if (sender == room) {
          val json = Json.obj(
            "kind" -> kind,
            "id" -> user.id.toString,
            "user" -> user.name,
            "date" -> Cfg.date,
            "message" -> text,
            "members" -> online.map(_.name).mkString(",")
          )
          out ! json
        }
      }
      case json: JsValue => {
        (json \ "text").validate[String] map {
          Utility.escape(_)
        } map {
          room ! Message(user, "talk", _ ,Nil)
        }
      }
      case other => log.error("unhandled: " + other)
  }
}

object UserActor {
  def props(user: User)(out: ActorRef) = Props(new UserActor(user, RoomActor(), out))
}
