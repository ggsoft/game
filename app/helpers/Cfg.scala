package helpers

import com.typesafe.config.ConfigFactory
import java.util.Date
import java.text.SimpleDateFormat
import play.api.mvc._

import models.User

object Cfg {

  val DF = "MM/dd/yyyy HH:mm:ss"
  def date: String = new SimpleDateFormat(DF).format(new Date)

  // load file <conf/application.conf>
  val conf = ConfigFactory.load

  def get(key: String, default: String = ""): String = {
    if (conf.hasPath(key)) conf.getString(key) else default
  }

  def getInt(key: String, default: Int = 0): Int = {
    if (conf.hasPath(key)) conf.getInt(key) else default
  }

  def getLong(key: String, default: Long = 0l): Long = {
    if (conf.hasPath(key)) conf.getLong(key) else default
  }

  def secret = get("play.crypto.secret")

  // load text file <fn> from <dir>
  def load(fn: String, dir: String, default: String = ""): String = {
    try {
      scala.io.Source.fromFile(dir+"/"+fn).mkString
    } catch {case _: Throwable => default}
  }

  def crc(s: String): String = {
    import java.util.zip.CRC32
    val crc32 = new CRC32
    crc32.update(s.getBytes)
    crc32.getValue+""
  }

  def md5(s: String): String = {
    import java.security.MessageDigest
    MessageDigest.getInstance("MD5").digest(s.getBytes("utf-8")).map("%02x".format(_)).mkString
  }

  def sha1(s: String): String = {
    import java.security.MessageDigest
    MessageDigest.getInstance("SHA-1").digest(s.getBytes("utf-8")).map("%02x".format(_)).mkString
  }

  def user(session: Session): Option[User] = {
    session.get("user").map(hash => User.user(hash))
  }

}
