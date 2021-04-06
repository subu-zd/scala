package exercises_adv

import scala.annotation.tailrec

trait MySet[A] extends (A => Boolean) {
  /*
  * EXERCISE - implement a functional set */

  def apply(elem: A): Boolean = contains(elem)

  def contains(elem: A): Boolean

  def +(elem: A): MySet[A]

  def ++(anotherSet: MySet[A]): MySet[A] // union

  def map[B](f: A => B): MySet[B]

  def flatMap[B](f: A => MySet[B]): MySet[B]

  def filter(predicate: A => Boolean): MySet[A]

  def foreach(f: A => Unit): Unit

  /* EXERCISE
  * removing an element
  * intersection with another set
  * difference with another set
  */

  def -(elem: A): MySet[A]

  def --(anotherSet: MySet[A]): MySet[A] // difference

  def &(anotherSet: MySet[A]): MySet[A] // intersection

  def unary_! : MySet[A] // negation of a set
}

// all elements of type A which satisfy a property
// { x in A | property(x) }
class PropertyBasedSet[A](property: A => Boolean) extends MySet[A] {
  def contains(elem: A): Boolean = property(elem)

  // { x in A | property(x) } + element = { x in A | property(x) || x == element }
  def +(elem: A): MySet[A] = new PropertyBasedSet[A](x => property(x) || x == elem)

  // { x in A | property(x) } ++ set => { x in A | property(x) || set contains x }
  def ++(anotherSet: MySet[A]): MySet[A] = new PropertyBasedSet[A](x => property(x) || anotherSet(x))

  // mapping or flatMapping a property-based set is a very deep rabbit hole as we don't know what to expect
  // e.g. from all integers => (_ % 3) [0 1 2]
  // but the problem that'll arise is that after we map, we won't know if this is actually a finite set or and
  // hence, we can't really test if an element is in the set or not
  // which basically defeats the whole purpose of a SET
  def map[B](f: A => B): MySet[B] = politelyFail

  def flatMap[B](f: A => MySet[B]): MySet[B] = politelyFail

  def filter(predicate: A => Boolean): MySet[A] = new PropertyBasedSet[A](x => property(x) && predicate(x))

  def foreach(f: A => Unit): Unit = politelyFail

  def -(elem: A): MySet[A] = filter(x => x != elem)

  def --(anotherSet: MySet[A]): MySet[A] = filter(!anotherSet(_))

  def &(anotherSet: MySet[A]): MySet[A] = filter(anotherSet(_))

  def unary_! : MySet[A] = new PropertyBasedSet[A](x => !property(x))

  def politelyFail = throw new IllegalArgumentException("Really deep rabbit hole")
}

class EmptySet[A] extends MySet[A] {
  def contains(elem: A): Boolean = false

  def +(elem: A): MySet[A] = new NonEmptySet[A](elem, this)

  def ++(anotherSet: MySet[A]): MySet[A] = anotherSet

  def map[B](f: A => B): MySet[B] = new EmptySet[B]

  def flatMap[B](f: A => MySet[B]): MySet[B] = new EmptySet[B]

  def filter(predicate: A => Boolean): MySet[A] = this

  def foreach(f: A => Unit): Unit = ()

  def -(elem: A): MySet[A] = this

  def --(anotherSet: MySet[A]): MySet[A] = this

  def &(anotherSet: MySet[A]): MySet[A] = this

  def unary_! : MySet[A] = new PropertyBasedSet[A](_ => true)
}

class NonEmptySet[A](head: A, tail: MySet[A]) extends MySet[A] {
  def contains(elem: A): Boolean = elem == head || tail.contains(elem)

  def +(elem: A): MySet[A] = {
    if(this contains elem) this
    else new NonEmptySet[A](elem, this)
  }

  def ++(anotherSet: MySet[A]): MySet[A] = tail ++ anotherSet + head
  /*
  * [1 2 3] ++ [4 5] =
  * [2 3] ++ [4 5] + 1 =
  * [3] ++ [4 5] + 1 + 2 =
  * [] ++ [4 5] + 1 + 2 + 3 =
  * [4 5] + 1 + 2 + 3 = [4 5 1 2 3] */

  def map[B](f: A => B): MySet[B] = (tail map f) + f(head)

  def flatMap[B](f: A => MySet[B]): MySet[B] = (tail flatMap f) ++ f(head)

  def filter(predicate: A => Boolean): MySet[A] = {
    val filteredTail = tail filter predicate
    if(predicate(head)) filteredTail + head
    else filteredTail
  }

  def foreach(f: A => Unit): Unit = {
    f(head)
    tail foreach f
  }

  def -(elem: A): MySet[A] =
    if(head == elem) tail
    else tail - elem + head

  def --(anotherSet: MySet[A]): MySet[A] = filter(!anotherSet(_))

  def &(anotherSet: MySet[A]): MySet[A] = filter(anotherSet(_)) // intersection == filter

  def unary_! : MySet[A] = new PropertyBasedSet[A](!this.contains(_))
}

object MySet {

  /*
  * val s = MySet(1, 2, 3) = buildSet(seq(1, 2, 3), [])
  * = buildSet(seq(2, 3), [] + 1)
  * = buildSet(seq(3), [1] + 2)
  * = buildSet(seq(), [1, 2] + 3)
  * = [1, 2, 3]
  * */

  def apply[A](values: A*): MySet[A] = {
    @tailrec
    def buildSet(valSeq: Seq[A], acc: MySet[A]): MySet[A] =
      if(valSeq.isEmpty) acc
      else buildSet(valSeq.tail, acc + valSeq.head)

    buildSet(values, new EmptySet[A])
  }
}

object MySetApp extends App {
  val s = MySet(1, 2, 3, 4)
  s + 5 ++ MySet(-1, -2) + 3 flatMap (x => MySet(x, 10 * x)) filter (_ % 2 == 0) foreach println

  val negative = !s //s .unary_! = all the naturals not equal to 1, 2, 3, 4
  println(negative(2))
  println(negative(5))

  val negativeEven = negative.filter(_ % 2 == 0)
  println(negativeEven(5))

  val negativeEven5 = negativeEven + 5
  println(negativeEven5(5))
}
