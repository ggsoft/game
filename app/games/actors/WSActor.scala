package games.actors

import akka.actor._
import akka.event.LoggingReceive

class WSActor (out: ActorRef) extends Actor with ActorLogging {

  override def preStart() = {
    System() ! "ws"
  }

  def receive = LoggingReceive {
    case msg: String => out ! msg
  }

}

object WSActor {
  def props(out: ActorRef) = Props(new WSActor(out))
}

