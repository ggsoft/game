package controllers

import games._
import games.actors._
import akka.actor.Props
import models._
import play.api.mvc._
import play.api.Play.current
import play.libs.Akka

import scala.util.Random

object Game extends Controller {

  var testGame: Option[TestGame] = None

  def gameRoomJs = Action { implicit request =>
    Ok(views.js.game(request))
  }

  def game = WebSocket.acceptWithActor[String, String] { request  => out =>
    WSActor.props(out)
  }

  def start = Action {
    val tg = new TestGame(6, new GameMap(100))
    tg.gameActor ! "init"
    testGame = Some(tg)
    Random.shuffle(User.list).foreach(u => Akka.system().actorOf(Props(new PlayerActor(u,tg))))
    Ok("")
  }

  def fill = Action {
    Ok(testGame.map(_.toHtml).getOrElse(""))
  }

  def restart(id: Int) = Action {
    testGame.map(_.replay(id))
    Ok("")
  }

}
