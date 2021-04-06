package lectures.part3_fp

import scala.util.Random

object Options extends App {
  val firstOption: Option[Int] = Some(4) // the declaration means the that this may or may not contain an Int
  val noOption: Option[Int] = None

  println(firstOption)

  // Options were invented to deal with unsafe APIs

  /*
  * def unsafeDef(): String = null
  * val res = Some(unsafeDef())
  *
  * // WRONG as Some() should always have a valid value inside
  * */

  def unsafeDef(): String = null

  val res = Option(unsafeDef()) // the Options apply() method takes care of deciding if the value is Some or None
  println(res) // None
  // the whole point of Options is that the programmer should never do the NULL checks

  // chained methods

  def safeDef(): String = "a valid result"
  val chainRes = Option(unsafeDef()).orElse(Option(safeDef()))

  // designing APIs using Options
  def betterUnsafe(): Option[String] = None
  def betterSafe(): Option[String] = Some("A valid result")

  val betterRes = betterUnsafe() orElse betterSafe()

  // functions on Options
  println(firstOption.isEmpty) // false: tests whether an Options has a value or not
  println(firstOption.get) // tries to retrieve a value from the Option. This is however UNSAFE as it resembles trying to access a NULL Pointer
  // avoid using "get" as it defeats the purpose of Options

  // map, flatMpa, filter
  println(firstOption.map(_ * 2))
  println(firstOption.filter(_ > 10))
  println(firstOption.flatMap(x => Option(x * 10)))
  // similarly Options also work with for-comprehensions

  // EXERCISE
  val config: Map[String, String] = Map(
    // fetched from elsewhere. as a result of which you don't have certainty if you have any values in the keys "host" and "port"
    "host" -> "176.45.36.1",
    "port" -> "80"
  )

  class Connection {
    def connect = "Connected"
  }

  object Connection {
    val random = new Random(System.nanoTime())

    def apply(host: String, port: String): Option[Connection] = {
      if(random.nextBoolean()) Some(new Connection)
      else None
    }
  }

  // try to establish a connection
  // if you can, then print the connect method

  val host = config.get("host") // .get() is an Option[String]
  val port = config.get("port")

  /*
  connection logic

  * if "host" exists
  *   if "post" exists
  *     then return Connection.apply(host, port)
  *
  * return null
  * */
  val connection = host.flatMap(h => port.flatMap(p => Connection.apply(h, p)))

  /*
  connectionStatus logic

  * if (c != null)
  *   return c.connect
  *
  * return null
  * */

  val connectionStatus = connection.map(c => c.connect)

  // if(connectionStatus == null) println(None)
  // else println(Some(connectionStatus.get))
  println(connectionStatus)

  /*
  * if(status != null)
  *   println(status)
  * */
  connectionStatus.foreach(println)


  // another solution using chained calls
  config.get("host")
    .flatMap(host => config.get("port")
      .flatMap(port => Connection(host, port))
      .map(connection => connection.connect))
    .foreach(println)

  // solution using for-comprehensions

  val forConnectionStatus = for {
    host <- config.get("host")
    port <- config.get("port")
    connection <- Connection(host, port)
  } yield {
    connection.connect
  }

  forConnectionStatus.foreach(println)
}
