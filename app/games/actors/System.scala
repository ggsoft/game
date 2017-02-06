package games.actors

import akka.actor._
import akka.event.LoggingReceive
import play.libs.Akka

class System extends Actor with ActorLogging {

  def receive = LoggingReceive {
    case "ws"  => System.ws = Some(sender)
  }
}

object System {

  var ws: Option[ActorRef] = None

  lazy val sys = {
    Akka.system().actorOf(Props[System])
  }
  def apply() = sys

}
