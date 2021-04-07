package lectures.part6_adv_fp

object LazyEvaluation extends App {

  lazy val x: Int = throw new RuntimeException
  // w/o the keyword "lazy", this program will expectedly crash
  // by adding the keyword lazy, it won't because
  // lazy values are evaluated once but only when they're used for the first time
  // essentially delaying the evaluation of values

//  println(x)

  // lazy values are only evaluated once
  val y: Int = {
    println("Lazy")
    42
  }

  println(y)
  println(y) // the second statement will only print 42

  // examples of implication
  def sideEffectCondition: Boolean = {
    println("Boo")
    true
  }

  def simpleCondition: Boolean = false

  lazy val lazyCondition = sideEffectCondition

  println(if(simpleCondition && lazyCondition) "YES" else "NO")
  // in this case the side effect "Boo" is not printed as the compiler doesn't evaluate the lazy val
  // since it encounters a false in simpleCondition and straight-away goes to the else block

  // in conjunction with call by name
  def byNameMethod(n: => Int): Int = n + n + n + 1
  def retrieveValue = {
    println("waiting")
    Thread.sleep(1000)
    42
  }

//  println(byNameMethod(retrieveValue))

  // here the waiting time was 3 seconds instead of 1
  // as the byName method evaluates 3 times
  // therefore, it doesn't make sense to use byName and still evaluate it multiple times

  // so in practice, we instead use lazy vals
  def byNameLazy(n: => Int) = {
    lazy val t = n
    t + t + t + 1
  }
  // here we end up waiting for only 1 sec
  // this technique is called call-by-need
  // it is useful when you want to evaluate a parameter only when needed
  // but want to reuse the same values in the rest of the code
  println(byNameLazy(retrieveValue))

  // filtering with lazy vals
  def lessThan30(i: Int): Boolean = {
    println(s"Is $i less than 30?")
    i < 30
  }

  def greaterThan20(i: Int): Boolean = {
    println(s"Is $i greater than 20?")
    i > 20
  }

  val numbers = List(1, 25, 40, 5, 23)
  val lt30 = numbers.filter(lessThan30) // List(1, 25, 5, 23)
  val gt20 = lt30.filter(greaterThan20)
  println(gt20)

  val lt30Lazy = numbers.withFilter(lessThan30) // withFilter() is a function on collections that uses lazy values under the hood
  val gt20Lazy = lt30Lazy.withFilter(greaterThan20)
  println()
  // for the LOC: println(gt20Lazy) in the console, we don't see any side-effects being applied
  // the filtering actually never takes place and the lessThan30 and greaterThan20 methods aren't actually being called
  println(gt20Lazy.foreach(println))
  // here the order of printing is difference as the side effects and the predicates are being checked on a by need basis

  // for-comprehensions use filters with guards
  for {
    a <- List(1, 2, 3) if a % 2 == 0 // if guards use lazy vals
  } yield a + 1
  // the above translates to below
  List(1, 2, 3).withFilter(_ % 2 == 0).map(_ + 1)

  /*EXERCISE
  *
  * a lazily evaluated, singly linked STREAM of elements
  * STREAMS are a special kind of collections in that the head of the stream is always evaluated and always available
  * but the tail of the stream is always lazily evaluated and available only on demand
  *
  * e.g.
  * naturals = MyStream.from(1)(x => x + 1) = stream of natural numbers (potentially infinite)
  * naturals.take(100) // lazily evaluated stream of the first 100 naturals (finite stream)
  * naturals.foreach(println) // will crash coz infinite
  * naturals.map(_ * 2) // stream of all even numbers (potentially infinite)
  *
  * */

  abstract class MyStream[+A] {
    def isEmpty: Boolean
    def head: A
    def tail: MyStream[A]

    def #::[B >: A](element: B): MyStream[B]
    def ++[B >: A](anotherStream: MyStream[B]): MyStream[B] //concatenate two streams

    def foreach(f: A => Unit): Unit
    def map[B](f: A => B): MyStream[B]
    def flatMap[B](f: A => MyStream[B]): MyStream[B]
    def filter(predicate: A => Boolean): MyStream[A]

    def take(n: Int): MyStream[A] // takes the first n elements out of the stream and returns the stream of those n numbers
    def takeAsList(n: Int): List[A]
  }

  object MyStream {
    def from[A](start: A)(generator: A => A): MyStream[A] = ???
  }
}
