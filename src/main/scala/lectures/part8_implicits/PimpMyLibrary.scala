package lectures.part8_implicits

import scala.annotation.tailrec
import scala.language.implicitConversions

object PimpMyLibrary extends App {
  // Enrichment allows us to decorate existing classes that we may not have access to with additional methods and properties
  // E.g. 2.isPrime

  implicit class RichInt(val value: Int) extends AnyVal { // this inheritance is done for memory optimisation purposes
    def isEven: Boolean = value % 2 == 0
    def sqrt: Double = Math.sqrt(value)
    def times(function: () => Unit): Unit = {
      @tailrec
      def timesAux(n: Int): Unit = {
        if (n <= 0) ()
        else {
          function()
          timesAux(n - 1)
        }
      }

      timesAux(value)
    }

    def *[T](list: List[T]): List[T] = {
      @tailrec
      def concatenate(n: Int, acc: List[T]): List[T] =
        if (n <= 0) acc
        else concatenate(n - 1, acc ++ list)

      concatenate(value, List())
    }
  }

  new RichInt(42).sqrt

  42.isEven // type enrichment = pimping
  // when the compiler reads this line, it interprets it as an error in the first place but doesn't give up,
  // it searches for all the implicit classes or all the implicit conversions that can wrap a type Int into something that contains the method isEven

  // the compiler then inserts an invisible constructor call in this case
  // basically rewrites 42.isEven as new RichInt(42).isEven

  1 to 10

  import scala.concurrent.duration._
  3.seconds
  /*
   * The implicit conversion between Int and DurationConversions and scala.RichInt,
   * is done with implicit methods buried within the code, but the principle is exactly the same */

  // compiler doesn't do multiple implicit searches
  implicit class RicherInt(richInt: RichInt) {
    def isOdd: Boolean = richInt.value % 2 != 0
  }
//  42.isOdd => doesn't compiler

  /* Enrich a String class
   * asInt
   * encrypt
   * - implements a caesar cypher
   * - John -> Lqjp (2 characters forward)
   *
   * Keep enriching the Int class
   * times(function)
   * - 3.times(() => ...)
   * '*' (multiply)
   * - 3 * List(1, 2) => List(1, 2, 1, 2, 1, 2) */

  implicit class RichString(str: String) {
    def asInt: Int = Integer.valueOf(str)
    def encrypt(cypherDistance: Int): String = str.map(ch => (ch + cypherDistance).asInstanceOf[Char])
  }

  println("3".asInt + "4".asInt)
  println("John".encrypt(2))

  3.times(() => println("Scala rocks!"))
  println(4 * List(1, 2))

  // "3" / 4
  implicit def stringToInt(str: String): Int = Integer.valueOf(str)

  println("6" / 2) // stringToInt(6) / 2

  // implicit classes are basically plain classes with an implicit conversion
  // equivalent -> implicit class RichAltInt(valueL Int)
  class RichAltInt(value: Int)
  implicit def enrich(value: Int): RichAltInt = new RichAltInt(value)

  // although implicit conversions with methods are generally more powerful, they are often discouraged
  implicit def intToBoolean(i: Int): Boolean = i == 1
  /*
   * if (n) do something
   * else do something else */

  val aConditionValue = if (3) "OK" else "Something wrong"
  println(aConditionValue)
  // this prints "Something wrong" because our implicit conversion method return true only if the number is equal to 1
  // the problem here and the whole danger zone in implicit conversions with methods is that if there is a bug in an implicit conversion,
  // with a method, it's super hard to trace it back.
  // implicit methods tend to be part of library packages, which are generally in a very different part of the codebase (and likely written by different people)

}
