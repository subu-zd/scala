package exercises_adv

import scala.annotation.tailrec

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
  def ++[B >: A](anotherStream: => MyStream[B]): MyStream[B] //concatenate two streams

  def foreach(f: A => Unit): Unit
  def map[B](f: A => B): MyStream[B]
  def flatMap[B](f: A => MyStream[B]): MyStream[B]
  def filter(predicate: A => Boolean): MyStream[A]

  def take(n: Int): MyStream[A] // takes the first n elements out of the stream and returns the stream of those n numbers
  def takeAsList(n: Int): List[A] = take(n).toList()

  /*
  * [1 2 3].toList([])
  * [2 3].toList([1])
  * [3].toList([2 1])
  * [].toList([3 2 1])
  * = [1 2 3]
  * */
  @tailrec
  final def toList[B >: A](acc: List[B] = Nil): List[B] =
    if(isEmpty) acc.reverse
    else tail.toList(head :: acc)
}

object EmptyStream extends MyStream[Nothing] {
  def isEmpty: Boolean = true
  def head: Nothing = throw new NoSuchElementException
  def tail: MyStream[Nothing] = throw new NoSuchElementException

  def #::[B >: Nothing](element: B): MyStream[B] = new Cons(element, this)
  def ++[B >: Nothing](anotherStream: => MyStream[B]): MyStream[B] = anotherStream

  def foreach(f: Nothing => Unit): Unit = ()
  def map[B](f: Nothing => B): MyStream[B] = this
  def flatMap[B](f: Nothing => MyStream[B]): MyStream[B] = this
  def filter(predicate: Nothing => Boolean): MyStream[Nothing] = this

  def take(n: Int): MyStream[Nothing] = this
}

class Cons[+A](hd: A, tl: => MyStream[A]) extends MyStream[A] {
  def isEmpty: Boolean = false
  override val head: A = hd
  override lazy val tail: MyStream[A] = tl // combining a call-by-name param with a lazy val is called call-by-need

  /*
  * val s = new Cons(1, EmptyStream)
  * val prepend = 1 #:: s = new Cons(1, s)
  * */
  def #::[B >: A](element: B): MyStream[B] = new Cons(element, this)
  def ++[B >: A](anotherStream: => MyStream[B]): MyStream[B] = new Cons(head, tail ++ anotherStream)

  def foreach(f: A => Unit): Unit = {
    f(head)
    tail.foreach(f)
  }

  def map[B](f: A => B): MyStream[B] = new Cons(f(head), tail.map(f)) // preserves lazy evaluation

  /*
  * s = new Cons(1, ?)
  * mapped = s.map(_ + 1) = new Cons(2, s.tail.map(_ + 1))
  *
  * "s.tail.map(_ + 1)" however, won't be evaluated till the time we use mapped.tail in some later expression
  *
  * */

  def flatMap[B](f: A => MyStream[B]): MyStream[B] = f(head) ++ tail.flatMap(f)

  def filter(predicate: A => Boolean): MyStream[A] =
    if(predicate(head)) new Cons(head, tail.filter(predicate))
    else tail.filter(predicate)

  def take(n: Int): MyStream[A] = {
    if(n <= 0) EmptyStream
    else if (n == 1) new Cons(head, EmptyStream)
    else new Cons(head, tail.take(n - 1))
  }
}

object MyStream {
  def from[A](start: A)(generator: A => A): MyStream[A] =
    new Cons(start, MyStream.from(generator(start))(generator))
}

object StreamsPG extends App {
  val naturals = MyStream.from(1)(_ + 1)
  println(naturals.head)
  println(naturals.tail.head)
  println(naturals.tail.tail.head)

  val startFrom0 = 0 #:: naturals // naturals.#::(0)
  println(startFrom0.head)

  startFrom0.take(10000).foreach(println)

  println(startFrom0.map(_ * 2).take(100).toList())

  println(startFrom0.flatMap( x => new Cons(x, new Cons(x + 1, EmptyStream)) ).take(10).toList())
  // this statement will throw a stack overflow error as flatMap is called recursively a lot of times
  // the reason it would cause a stack overflow error is because the expression "tail.flatMap(f)" should be lazily evaluated but is getting evaluated eagerly
  // this means the "++" operator is not preserving lazy evaluation

  println(startFrom0.filter(_ < 10).take(10).toList())
  // NOTE: println(startFrom0.filter(_ < 10).take(10).toList())
  // take(11) will cause an error coz after meeting, the condition of the predicate i.e. < 10,
  // the compiler will keep on recursively looking for the next element which satisfies the predicate

  /* EXERCISE on streams
  *
  * 1. stream of fibonacci numbers
  * 2. stream of prime numbers with Eratosthenes' sieve
  *
  * [2 3 4 ...]
  * - filter out all numbers divisible by 2 => [2 3 5 7 9 11 ...]
  * - filter out all numbers divisible by 3 => [2 3 5 7 11 13 ...]
  * - filter out all numbers divisible by 5 => [2 3 5 7 ...]
  *
  * */

  def fibonacci(first: BigInt, second: BigInt): MyStream[BigInt] = {
    new Cons(first, fibonacci(second, first + second))
  }

  def eratosthenes(numbers: MyStream[Int]): MyStream[Int] = {
    if (numbers.isEmpty) numbers
    else new Cons(numbers.head, eratosthenes(numbers.tail.filter(_ % numbers.head != 0)))
  }

  println(fibonacci(1, 1).take(100).toList())

  println(eratosthenes(MyStream.from(2)(_ + 1)).take(20).toList())
}
