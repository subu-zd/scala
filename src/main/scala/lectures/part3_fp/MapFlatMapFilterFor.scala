package lectures.part3_fp

object MapFlatMapFilterFor extends App {
  val list = List(1, 2, 3)
  println(list)
  println(list.head)
  println(list.tail)

  // map
  println(list.map(_ + 1))
  println(list.map(_ + "is a number"))

  // filter
  println(list.filter(_ % 2 == 0))

  // flat map
  val toPair = (x: Int) => List(x, x + 1)
  println(list.flatMap(toPair))

  /* Print all combinations between two lists */

  // iterations
  val nos = List(1, 2, 3, 4)
  val chars = List('a', 'b', 'c', 'd')
  val colours = List("black", "white")

  val combinations = nos.flatMap(n => chars.flatMap(c => colours.map(colour => "" + c + n + "-" + colour)))
  println(combinations)

  // foreach

  list.foreach(println)

  // iterating in Scala can lead to a very complex code and hence as a shorthand we have:
  // for-comprehensions

  val forCombinations = for {
    // to filter out values based on condition, we can use GUARDS. These are translated into filter() calls at the compiler level
    n <- nos if n % 2 == 0
    c <- chars
    colour <- colours
  } yield "" + c + n + "-" + colour
  // this for-yield block gets converted into flatMap() and map() calls at the compiler level

  println(forCombinations)

  println(list.map { x => x * 2 })

  /* EXERCISES
  * 1. MyList supports for comprehensions?
  *
  *   map(f: A => B) => MyList[B]
  *   filter(p: A => Boolean) => MyList[A]
  *   flatMap(f: A => MyList[B]) => MyList[B]
  *
  * the function signatures in MyList are identical to the ones required by the standard functions(map, flaMap etc...) that come along with the inbuilt Scala List
  * hence, for-comprehensions will work with MyList
  *
  * 2. implement a small collection of at most ONE element - Maybe[+T]
  *     - the only possible subtypes of this are going to be an empty collection or an element with one element of type T contained
  * "Maybe" is often used in FP to denote optional values (!= type Option)
  * */
}
