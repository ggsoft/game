package games

import akka.actor.{ActorRef, Props}
import games.actors._
import models.User
import play.libs.Akka

import scala.collection.mutable.ListBuffer
import scala.util.Random

trait Game {
  val count: Int // count of players
  val gmap: GameMap // map with areas to capture
  var rounds = 0
  val order = Random.shuffle((0 to count-1)).toList // random turn order
  val history = ListBuffer[Message]()

  val gameActor: ActorRef
  val roundActor: ActorRef
}

class TestGame(val count: Int, val gmap: GameMap) extends Game {

  val gameActor =  Akka.system().actorOf(Props(new GameActor(this)))
  val roundActor =  Akka.system().actorOf(Props(new RoundActor(this)))
  val players = ListBuffer[User]()

  private def toHtml(l: List[Message], s: String, k: Int): String = {
    l match {
      case head :: tail => toHtml(tail, s + "<option value=\""+k+"\">"+head.info+"</option>",k+1)
      case Nil => s
    }
  }
  def toHtml: String = toHtml(history.toList,"",1)

  def replay(n: Int)  = {
    val out = System.ws.get
    history.take(n).foreach(m => {
      m.mess match {
        case "init" => {
          gmap.init
          players.clear
          rounds = 0
          out ! m.info
        }
        case Join(user) => {
          players += user
          out ! m.info
        }
        case "round" => {
          rounds +=1
          out ! m.info
        }
        case "start" => {
          out ! "Game is started now"
        }
        case "end" => {
          out ! "Game is over"
          val result = players.map(u => (u, gmap.count(u)))
          result.foreach(p => out ! p._1+" get score "+p._2)
          val max = result.map(_._2).max
          out ! "Winners are: "
          result.filter(_._2 == max).map(_._1).foreach(u =>  out ! ""+u)
          out ! "-"*100
        }
        case Players(l) => {}
        case t: Turn => {
          out ! t.u.name+" try to grab "+t.r.name
          gmap.grab(t.r,t.u)
        }
      }
    })
    out ! "-"*100
  }

}

case class Message(mess: Any, info: String)
