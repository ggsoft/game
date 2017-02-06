package games.actors

import akka.actor._
import akka.event.LoggingReceive
import games._
import models.User
import scala.util.Random

class PlayerActor(user: User, g: Game) extends Actor with ActorLogging {

  val out = System.ws.get

  override def preStart() = {
    g.gameActor ! Join(user)
  }

  def receive = LoggingReceive {
    case "turn" => {
      if (g.gmap.list.size>0) {
        val index = Random.nextInt(g.gmap.list.size)
        val t = Turn(user, g.gmap.list(index))
        out ! t.u + " try to grab " + t.r.name
        g.gmap.grab(t.r, user)
        g.history += Message(t, "turn " + user)
        sender ! t
      } else g.gameActor ! "end"
    }
    case "stop" =>  context stop self
    case other => log.error("unhandled: " + other)
  }
}
