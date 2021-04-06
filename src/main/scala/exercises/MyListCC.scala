package exercises

abstract class MyListCC[+A] {

  def getHead: A

  def getTail: MyListCC[A]

  def isEmpty: Boolean

  def add[B >: A](n: B): MyListCC[B]

  def printNodes: String

  override def toString: String = "[" + printNodes + "]"

  def map[B](transformer: TransformerCC[A, B]): MyListCC[B]

  def filter(predicate: PredicateCC[A]): MyListCC[A]

  def flatMap[B](transformer: TransformerCC[A, MyListCC[B]]): MyListCC[B]

  def ++[B >: A](list: MyListCC[B]): MyListCC[B]
}

case object EmptyCC extends MyListCC[Nothing] {
  def getHead: Nothing = throw new NoSuchElementException

  def getTail: MyListCC[Nothing] = throw new NoSuchElementException

  def isEmpty: Boolean = true

  def add[B >: Nothing](n: B): MyListCC[B] = new ConsCC(n, EmptyCC)

  def printNodes: String = ""

  def map[B](transformer: TransformerCC[Nothing, B]): MyListCC[B] = EmptyCC

  def filter(predicate: PredicateCC[Nothing]): MyListCC[Nothing] = EmptyCC

  def flatMap[B](transformer: TransformerCC[Nothing, MyListCC[B]]): MyListCC[B] = EmptyCC

  def ++[B >: Nothing](list: MyListCC[B]): MyListCC[B] = list
}

case class ConsCC[+A](head: A, tail: MyListCC[A]) extends MyListCC[A] {
  def getHead: A = head

  def getTail: MyListCC[A] = tail

  def isEmpty: Boolean = false

  def add[B >: A](n: B): MyListCC[B] = new ConsCC(n, this)

  def printNodes: String = {
    if(tail.isEmpty) "" + head
    else head + "->" + tail.printNodes
  }

  def filter(predicate: PredicateCC[A]): MyListCC[A] = {
    if(predicate.test(head)) new ConsCC(head, tail.filter(predicate))
    else tail.filter(predicate)
  }


  def map[B](transformer: TransformerCC[A, B]): MyListCC[B] = {
    new ConsCC[B](transformer.transform(head), tail.map(transformer))
  }

  def flatMap[B](transformer: TransformerCC[A, MyListCC[B]]): MyListCC[B] = {
    transformer.transform(head) ++ tail.flatMap(transformer)
  }

  def ++[B >: A](list: MyListCC[B]): MyListCC[B] = new ConsCC[B](head, tail ++ list)
}

trait PredicateCC[-T] {
  def test(elem: T): Boolean
}

trait TransformerCC[-A, B] {
  def transform(elem: A): B
}

object MyListCCTest extends App {
  val l1: MyListCC[String] = new ConsCC("Hello", new ConsCC("Scala", EmptyCC))
  val clonel1: MyListCC[String] = new ConsCC("Hello", new ConsCC("Scala", EmptyCC))
  println(l1.toString)
  println(clonel1 == l1)

  val l2: MyListCC[Int] = new ConsCC(1, new ConsCC(2, new ConsCC(3, EmptyCC)))
  println(l2.toString)

  println(l2.map(new TransformerCC[Int, Int] {
    override def transform(n: Int): Int = n * 2
  }))

  println(l2.filter(new PredicateCC[Int] {
    override def test(n: Int): Boolean = n % 2 == 0
  }))

  val l3: MyListCC[Int] = new ConsCC(4, new ConsCC(5, EmptyCC))
  println(l3.toString)

  println(l2 ++ l3)
  println(l2.flatMap(new TransformerCC[Int, MyListCC[Int]] {
    override def transform(n: Int): MyListCC[Int] = new ConsCC(n, new ConsCC(n + 1, EmptyCC))
  }))


}
