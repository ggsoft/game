package games.actors

import akka.actor._
import akka.event.LoggingReceive
import games._
import models.User
import scala.collection.mutable.{Map => MMap}

class GameActor(val g: Game) extends Actor with ActorLogging {
  val players = MMap[ActorRef, User]()
  val out = System.ws.get
  def count = players.count(_ => true)

  def init = {
    players.keys.foreach(_ ! "stop")
    players.clear
  }

  def receive = LoggingReceive {
    case "stop" => {
      init
      g.roundActor ! "stop"
      context stop self
    }
    case "init" => {
      init
      g.history += Message("init", "init game")
    }
    case "round" => {
      g.rounds +=1
      if (g.gmap.list.size > 0) {
        out ! "round "+g.rounds
        g.history += Message("round", "round "+g.rounds)
        g.roundActor ! Players(players.map(_._1).toList)
      } else self ! "end"
    }
    case "end" => {
      g.history += Message("end", "end of game")
      out ! "Game is over"
      val result = players.map(_._2).map(u => (u, g.gmap.count(u)))
      result.foreach(p => out ! p._1+" get score "+p._2)
      val max = result.map(_._2).max
      out ! "Winners are: "
      result.filter(_._2 == max).map(_._1).foreach(u =>  out ! ""+u)
      out ! "-"*100
      self ! "stop"
    }
    case Join(user) => {
      if (count < g.count) {
        players(sender) = user
        out ! "joined "+user
        g.history += Message(Join(user), "join "+user)
        if (count == g.count)  {
          out ! "game is started now"
          g.history += Message("start", "game started")
          g.rounds = 1
          g.roundActor ! Players(players.map(_._1).toList)
          out ! "round "+g.rounds
          g.history += Message("round", "round "+g.rounds)
        }
      } else sender ! "stop"
    }
  }

}

case class Join(u: User)

