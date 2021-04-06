package exercises

abstract class MyListFP[+A] {

  def getHead: A

  def getTail: MyListFP[A]

  def isEmpty: Boolean

  def add[B >: A](n: B): MyListFP[B]

  def printNodes: String

  override def toString: String = "[" + printNodes + "]"

  def map[B](transformer: (A => B)): MyListFP[B]

  def filter(predicate: (A => Boolean)): MyListFP[A]

  def flatMap[B](transformer: (A => MyListFP[B])): MyListFP[B]

  def ++[B >: A](list: MyListFP[B]): MyListFP[B]
}

case object EmptyFP extends MyListFP[Nothing] {
  def getHead: Nothing = throw new NoSuchElementException

  def getTail: MyListFP[Nothing] = throw new NoSuchElementException

  def isEmpty: Boolean = true

  def add[B >: Nothing](n: B): MyListFP[B] = new ConsFP(n, EmptyFP)

  def printNodes: String = ""

  def map[B](transformer: (Nothing => B)): MyListFP[B] = EmptyFP

  def filter(predicate: (Nothing => Boolean)): MyListFP[Nothing] = EmptyFP

  def flatMap[B](transformer: (Nothing => MyListFP[B])): MyListFP[B] = EmptyFP

  def ++[B >: Nothing](list: MyListFP[B]): MyListFP[B] = list
}

case class ConsFP[+A](head: A, tail: MyListFP[A]) extends MyListFP[A] {
  def getHead: A = head

  def getTail: MyListFP[A] = tail

  def isEmpty: Boolean = false

  def add[B >: A](n: B): MyListFP[B] = new ConsFP(n, this)

  def printNodes: String = {
    if(tail.isEmpty) "" + head
    else head + "->" + tail.printNodes
  }

  def filter(predicate: (A => Boolean)): MyListFP[A] = {
    if(predicate(head)) new ConsFP(head, tail.filter(predicate))
    else tail.filter(predicate)
  }


  def map[B](transformer: (A => B)): MyListFP[B] = {
    new ConsFP[B](transformer(head), tail.map(transformer))
  }

  def flatMap[B](transformer: (A => MyListFP[B])): MyListFP[B] = {
    transformer(head) ++ tail.flatMap(transformer)
  }

  def ++[B >: A](list: MyListFP[B]): MyListFP[B] = new ConsFP[B](head, tail ++ list)
}

object MyListFPTest extends App {
  val l1: MyListFP[String] = new ConsFP("Hello", new ConsFP("Scala", EmptyFP))
  val clonel1: MyListFP[String] = new ConsFP("Hello", new ConsFP("Scala", EmptyFP))
  println(l1.toString)
  println(clonel1 == l1)

  val l2: MyListFP[Int] = new ConsFP(1, new ConsFP(2, new ConsFP(3, EmptyFP)))
  println(l2.toString)

  println(l2.map(new (Int => Int) {
    override def apply(n: Int): Int = n * 2
  }))

  println(l2.filter(new (Int => Boolean) {
    override def apply(n: Int): Boolean = n % 2 == 0
  }))

  val l3: MyListFP[Int] = new ConsFP(4, new ConsFP(5, EmptyFP))
  println(l3.toString)

  println(l2 ++ l3)
  println(l2.flatMap(new (Int => MyListFP[Int]) {
    override def apply(n: Int): MyListFP[Int] = new ConsFP(n, new ConsFP(n + 1, EmptyFP))
  }))


}

// EXERCISE NOTES

// MyList is now a FUNCTIONAL COLLECTION
// as it uses functions as first class values especially when it comes to map, flatmap and filet
// map, flatmap and filter are called HIGHER ORDER FUNCTIONS
// HOFs either receive other functions as parameters or return other functions as the result
