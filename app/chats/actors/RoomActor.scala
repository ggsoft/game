package chats.actors

import akka.actor._
import akka.event.LoggingReceive
import models.User
import play.libs.Akka

import scala.collection.mutable.{Map => MMap}

class RoomActor extends Actor with ActorLogging {
  val users = MMap[ActorRef, User]()

  def receive = LoggingReceive {
    case m: Message => users.keys map { _ ! m.copy(online=users.values.toSeq)}
    case Join(user) => {
      users(sender) = user
      update(user)
      context watch sender
    }
    case Quit(user) => {
      users -= sender
      update(user)
    }
    case Terminated(ref) => users -= ref
  }

  def update(user: User) = users.keys map { _ ! Message(user,"info","",users.values.toSeq)}

}

object RoomActor {
  lazy val room = Akka.system().actorOf(Props[RoomActor])
  def apply() = room
}

case class Message(user: User, kind: String, text: String, online: Seq[User])
case class Join(u: User)
case class Quit(u: User)
