package exercises

abstract class MyListLambda[+A] {

  def getHead: A

  def getTail: MyListLambda[A]

  def isEmpty: Boolean

  def add[B >: A](n: B): MyListLambda[B]

  def printNodes: String

  override def toString: String = "[" + printNodes + "]"

  def map[B](transformer: (A => B)): MyListLambda[B]

  def filter(predicate: (A => Boolean)): MyListLambda[A]

  def flatMap[B](transformer: (A => MyListLambda[B])): MyListLambda[B]

  def ++[B >: A](list: MyListLambda[B]): MyListLambda[B]
}

case object EmptyLambda extends MyListLambda[Nothing] {
  def getHead: Nothing = throw new NoSuchElementException

  def getTail: MyListLambda[Nothing] = throw new NoSuchElementException

  def isEmpty: Boolean = true

  def add[B >: Nothing](n: B): MyListLambda[B] = new ConsLambda(n, EmptyLambda)

  def printNodes: String = ""

  def map[B](transformer: (Nothing => B)): MyListLambda[B] = EmptyLambda

  def filter(predicate: (Nothing => Boolean)): MyListLambda[Nothing] = EmptyLambda

  def flatMap[B](transformer: (Nothing => MyListLambda[B])): MyListLambda[B] = EmptyLambda

  def ++[B >: Nothing](list: MyListLambda[B]): MyListLambda[B] = list
}

case class ConsLambda[+A](head: A, tail: MyListLambda[A]) extends MyListLambda[A] {
  def getHead: A = head

  def getTail: MyListLambda[A] = tail

  def isEmpty: Boolean = false

  def add[B >: A](n: B): MyListLambda[B] = new ConsLambda(n, this)

  def printNodes: String = {
    if(tail.isEmpty) "" + head
    else head + "->" + tail.printNodes
  }

  def filter(predicate: (A => Boolean)): MyListLambda[A] = {
    if(predicate(head)) new ConsLambda(head, tail.filter(predicate))
    else tail.filter(predicate)
  }


  def map[B](transformer: (A => B)): MyListLambda[B] = {
    new ConsLambda[B](transformer(head), tail.map(transformer))
  }

  def flatMap[B](transformer: (A => MyListLambda[B])): MyListLambda[B] = {
    transformer(head) ++ tail.flatMap(transformer)
  }

  def ++[B >: A](list: MyListLambda[B]): MyListLambda[B] = new ConsLambda[B](head, tail ++ list)
}

object MyListLambdaTest extends App {
  val l1: MyListLambda[String] = new ConsLambda("Hello", new ConsLambda("Scala", EmptyLambda))
  val clonel1: MyListLambda[String] = new ConsLambda("Hello", new ConsLambda("Scala", EmptyLambda))
  println(l1.toString)
  println(clonel1 == l1)

  val l2: MyListLambda[Int] = new ConsLambda(1, new ConsLambda(2, new ConsLambda(3, EmptyLambda)))
  println(l2.toString)

  println(l2.map(x => x * 2))

  println(l2.filter(n => n % 2 == 0))

  val l3: MyListLambda[Int] = new ConsLambda(4, new ConsLambda(5, EmptyLambda))
  println(l3.toString)

  println(l2 ++ l3)
  println(l2.flatMap(n => new ConsLambda(n, new ConsLambda(n + 1, EmptyLambda)) ))


}
