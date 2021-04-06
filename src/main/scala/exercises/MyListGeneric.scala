package exercises

abstract class MyListGen[+A] {

  def getHead: A

  def getTail: MyListGen[A]

  def isEmpty: Boolean

  def add[B >: A](n: B): MyListGen[B]

  def printNodes: String

  override def toString: String = "[" + printNodes + "]"
}

object EmptyGen extends MyListGen[Nothing] {
  def getHead: Nothing = throw new NoSuchElementException

  def getTail: MyListGen[Nothing] = throw new NoSuchElementException

  def isEmpty: Boolean = true

  def add[B >: Nothing](n: B): MyListGen[B] = new ConsGen(n, EmptyGen)

  def printNodes: String = ""
}

class ConsGen[+A](head: A, tail: MyListGen[A]) extends MyListGen[A] {
  def getHead: A = head

  def getTail: MyListGen[A] = tail

  def isEmpty: Boolean = false

  def add[B >: A](n: B): MyListGen[B] = new ConsGen(n, this)

  def printNodes: String = {
    if(tail.isEmpty) "" + head
    else head + "->" + tail.printNodes
  }
}

object GenListTest extends App {
  val l1: MyListGen[String] = new ConsGen("Hello", new ConsGen("Scala", EmptyGen))
  println(l1.toString)

  val l2: MyListGen[Int] = new ConsGen(1, new ConsGen(2, new ConsGen(3, EmptyGen)))
  println(l2.toString)
}

