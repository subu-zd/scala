package lectures.part4_pm

object PatternsEverywhere extends App {
  // big idea #1
  try {
    // code
  } catch {
    case e: RuntimeException => "runtime"
    case npe: NullPointerException => "NPE"
    case _ => "some other error"
  }

  // "catch" blocks are actually Pattern Matches
  /*
  * try {
  *   //code
  * } catch (err) {
  *   err match {
  *     case e: RuntimeException => "runtime"
  *     case npe: NullPointerException => "NPE"
  *     case _ => "some other error"
  *   }
  * }
  * */

  // big idea #2
  val list = List(1, 2, 3, 4)
  val evenOnes = for {
    x <- list if x % 2 == 0
  } yield 10 * x

  // "generators" are also based on Pattern Matching
  val tuples = List((1, 2), (3, 4))
  val filterTuples = for {
    (first, second) <- tuples
  } yield first * second
  // case classes, :: operators etc.

  // big idea #3
  val tuple = (1, 2, 3)
  val (a, b, c) = tuple
  // this assigns multiple values by exploiting the name binding property of pattern matching
  // in this case a = 1, b = 2, c = 3

  val head :: tail = list
  println(head)
  println(tail)

  // big idea #4
  // partial function
  val mappedList = list.map {
    case v if v % 2 == 0 => s"$v is even"
    case 1 => "the one"
    case _ => "something else"
  } // partial function literal

  // this is equivalent to =>
  /*
  * val mappedList = list.map { x => x.match {
        case v if v % 2 == 0 => s"$v is even"
        case 1 => "the one"
        case _ => "something else"
      }
    }
  * */

  println(mappedList) // every single element is mapped and put through all the cases
}
