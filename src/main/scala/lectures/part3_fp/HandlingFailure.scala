package lectures.part3_fp

import scala.util.{Failure, Random, Success, Try}

object HandlingFailure extends App {

  val aSuccess = Success(3)
  val aFailure = Failure(new RuntimeException("SUPER FAILURE"))

  println(aSuccess)
  println(aFailure)

  // most of the time, you don't need to construct Success() and Failure() yourself because
  // the Try companion object's apply() method takes care of that

  def unsafeDef(): String = throw new RuntimeException("No String")

  val potentialFailure = Try(unsafeDef()) // calling an unsafe method didn't crash the program
  // because Try() caught the exception wrapped it up in Failure()
  println(potentialFailure)

  // syntax sugar
  val anotherPotentialFailure = Try {
    // code that might throw

  }

  // utilities
  println(potentialFailure.isSuccess)

  // orElse
  def backupDef(): String = "A valid result"
  val fallbackTry = Try(unsafeDef()).orElse(Try(backupDef()))
  println(fallbackTry)

  // If you design the API
  def betterUnsafeDef(): Try[String] = Failure(new RuntimeException)
  def betterBackupDef(): Try[String] = Success("a valid result")
  val betterFallback = betterUnsafeDef() orElse betterBackupDef()
  println(betterFallback)

  // NOTE:
  // Whenever you feel that a block of code might return NULL then use OPTION
  // Whenever you feel that a block of code might THROW an EXCEPTION, use TRY

  // map, flatMap, filter
  println(aSuccess.map(_ * 2))
  println(aSuccess.flatMap(x => Success(x * 10)))
  println(aSuccess.filter(_ > 10))

  /*
  * EXERCISE
  *
  * if you get the HTML page from the connection, print it to the console i.e. call renderHTML()
  * */

  val host = "localhost"
  val port = "8080"
  def renderHTML(page: String): Unit = println(page)

  class Connection {
    def get(url: String): String = {
      val random = new Random(System.nanoTime())
      if(random.nextBoolean()) "<html>...</html>"
      else throw new RuntimeException("Connection interrupted")
    }

    def getSafe(url: String): Try[String] = Try(get(url))
  }

  object HttpService {
    val random = new Random(System.nanoTime())

    def getConnection(host: String, port: String): Connection = {
      if(random.nextBoolean()) new Connection
      else throw new RuntimeException("Port occupied")
    }

    def getSafeConnection(host: String, port: String): Try[Connection] = Try(getConnection(host, port))
  }

  val possibleConnection = HttpService.getSafeConnection(host, port)
  val possibleHTML = possibleConnection.flatMap(c => c.getSafe("/home"))
  possibleHTML.foreach(renderHTML)

  // shorthand version
  HttpService.getSafeConnection(host, port)
    .flatMap(connection => connection.getSafe("/home"))
    .foreach(renderHTML)

  // for-comprehension version
  for {
    connection <- HttpService.getSafeConnection(host, port)
    html <- connection.getSafe("/home")
  } yield {
    renderHTML(html)
  }

  /*
  * // In an imperative language
  *
  * try {
  *   connection = HttpService.getConnection(host, port)
  *   try {
  *     page = connection.get("/home")
  *     renderHTML(page)
  *   } catch (exception) {
  *
  *   }
  * } catch (exception) {
  *
  * }*/
}
