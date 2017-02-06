package games.actors

import akka.actor._
import akka.event.LoggingReceive
import games._
import models.User
import scala.collection.mutable.ListBuffer

class RoundActor(val g: Game) extends Actor with ActorLogging {

  val turns = ListBuffer[Turn]()
  var players: List[ActorRef] = Nil
  val out = System.ws.get

  def turn = {
    val k = g.order(turns.size)
    players(k) ! "turn"
  }

  def receive = LoggingReceive {
    case "stop" => context stop self
    case Players(l) => {
      turns.clear
      players = l
      turn
    }
    case t: Turn => {
      turns += t
      if (turns.size == g.count) {
        g.gameActor ! "round"
      } else turn
    }
  }
}

case class Turns(l: List[Turn])
case class Players(l: List[ActorRef])
case class Turn(u: User, r: Region)