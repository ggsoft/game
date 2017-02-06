package models

import scala.collection.mutable.{Map => MMap}

/*
  Only for testing purposes. Groups, DB connection, authorization - later
 */
case class User(id: Long, name: String, score: Int)

object User {

  private var n = 0l
  private def id = {
    n += 1
    n
  }
  private val m = MMap[String,User]()

  def user(hash: String): User = {
    m.get(hash).map(u => u).getOrElse {
      val uid = id
      val newUser = User(uid, "User " + uid, 0)
      m(hash) = newUser
      newUser
    }
  }

   val list = Seq(
    User(1,"John",25),
    User(2,"Kevin",51),
    User(3,"Jean",75),
    User(4,"Lynn",32),
    User(5,"Leslie",48),
    User(6,"Cynthia",83),
    User(7,"Daniel",18),
    User(8,"Karen",67),
    User(9,"Sarah",21),
    User(10,"Gregory",95),
    User(11,"Terry",15),
    User(12,"Chris",77)
  )

}
