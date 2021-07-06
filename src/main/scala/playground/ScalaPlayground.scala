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
  println(l.maxBy(_.resourceMultiplier))

  println(List(1, 2, 3) ++ List(5))

  val list1 = List(1, 2, 3, 4, 5, 6, 7)
  println(list1.filter(_ % 2 != 0))

  val (x, y) = list1.splitAt(list1.indexOf(2))
  println(x + " - " + y)

  val a :: b = List(1, 2, 3, 4, 5, 6, 7)
  println(a + " :: " + b)

  List(1, 2, 3).map(_ * 2)
}
