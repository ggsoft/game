package controllers

import helpers.Cfg
import play.api.mvc._

object Main extends Controller {

  def index = Action {
    val content = views.html.main()
    Ok(views.html.index(content))
  }

  def chat = Action { implicit request =>
    val userString = Cfg.secret + request.remoteAddress + request.headers.get("User-Agent").getOrElse("")
    val hash = Cfg.md5(userString)
    val content = views.html.chat()
    Ok(views.html.index(content)).withSession(
      "user" -> hash
    )
  }

  def game = Action {
    val content = views.html.game()
    Ok(views.html.index(content))
  }

}
