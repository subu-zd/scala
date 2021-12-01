package playground

import java.sql.Timestamp
import java.time.Instant
import java.time.temporal.ChronoUnit
import scala.math.Ordered.orderingToOrdered

object ScalaPlayground extends App {

  case class Job(
    resourceMultiplier: Int,
    category: String = "XS4"
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

  val oddNumbers :List[Int] = List(1,3,5,7,9,11,13)

//  def reduce[A1 >: Int](op: (A1, A1) => A1): A1

  def additionReduce(number1: Int, number2: Int) : Int = {
    number1 + number2
  }

  println(oddNumbers.reduceRight(additionReduce))

  def instantIsBefore(maybeInstant: Option[Instant], age: Int, unit: ChronoUnit): Boolean = {
    val threshold = Instant.now.minus(age, unit)

    maybeInstant match {
      // check if the execution is finished more than X minutes ago
      // to make sure all λ functions triggered by that execution are finished as well
      case Some(stopDate) => stopDate.isBefore(threshold)
      case None => false
    }
  }

  val temp = Timestamp.from(Instant.now.minus(120, ChronoUnit.MINUTES).truncatedTo(ChronoUnit.SECONDS))

  println(instantIsBefore(Some(temp.toInstant), 5400000, ChronoUnit.MILLIS))
  println(Timestamp.from(Instant.now.minus(2, ChronoUnit.HOURS).truncatedTo(ChronoUnit.SECONDS)))

//  def sanitiseEmojis(emojis: EmojiDefinition[]): EmojiDefinition[] {
//    val retVal: EmojiDefinition[] = [];
//    emojis.forEach((emoji) => {
//    if (supportsEmoji(emoji)) {
//    retVal.push(emoji);
//  }
//  });
//    return retVal;
//  }
//
//  val retVal = emojis.filter(emoji => supportEmoji(emoji))

  println(List(1, 1, 1, 5, 5, 5, 2, 3, 4, 5).filter(x => Seq(6, 5).contains(x)))

  val timeBool = Timestamp.from(Instant.now.minus(15, ChronoUnit.MINUTES)) <= Timestamp.from(Instant.now.minus(14, ChronoUnit.MINUTES))

  println("bool: " + timeBool)
  println(Timestamp.from(Instant.now.minus(15, ChronoUnit.MINUTES)))
  println(Timestamp.from(Instant.now.minus(14, ChronoUnit.MINUTES)))

  val testOpt1: Option[Job] = Some(Job(4, "XS3"))
  val testOpt2: Option[Job] = None

  println(testOpt1.map(_.resourceMultiplier).getOrElse(1))
  println(testOpt1.map(_.category).getOrElse("XS4"))
  println(testOpt2.map(_.resourceMultiplier).getOrElse(1))
  println(testOpt2.map(_.category).getOrElse("XS4"))


  println(List(1, 1, 1, 5, 5, 5, 2, 3, 4, 5).flatMap(a => List(a * 2)))
}
