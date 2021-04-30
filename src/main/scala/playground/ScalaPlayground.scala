package playground

import scala.util.Try

object ScalaPlayground extends App {

  case class Job(
    resourceMultiplier: Int
  )

  val j1 = Job(2)
  val j2 = Job(8)
  val j3 = Job(4)
  val j4 = Job(5)
  val j5 = Job(5)

  val l = List(j1, j2, j3, j4, j5)
  println(l.map(_.resourceMultiplier).max)
}
