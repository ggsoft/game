package games

import models.User

import scala.collection.mutable.{Map => MMap}

case class Region(id: Long, name: String)

class GameMap(n: Int) {

  private val l = MMap[Region,Option[User]]()
  init

  def init = {
    l.empty
    (1 to n).foreach(i => l+=Region(i,"Region "+i) -> None)
  }

  def grab(r: Region, u: User) = {
    if (l(r) == None) l(r) = Some(u)
    else if (l(r).get.score < u.score) l(r) = Some(u)
  }

  def list = l.filter(_._2 == None).map(_._1).toList

  def list(u: User) =  l.filter(_._2.map(_ == u).getOrElse(false)).map(_._1).toList

  def count(u: User) = list(u).size

}
