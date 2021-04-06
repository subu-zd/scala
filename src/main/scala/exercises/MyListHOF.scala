package exercises


abstract class MyListHOF[+A] {

  def getHead: A

  def getTail: MyListHOF[A]

  def isEmpty: Boolean

  def add[B >: A](n: B): MyListHOF[B]

  def printNodes: String

  override def toString: String = "[" + printNodes + "]"

  def map[B](transformer: (A => B)): MyListHOF[B]

  def filter(predicate: (A => Boolean)): MyListHOF[A]

  def flatMap[B](transformer: (A => MyListHOF[B])): MyListHOF[B]

  def ++[B >: A](list: MyListHOF[B]): MyListHOF[B]

  def foreach(f: A => Unit): Unit
  
  def sort(compare: (A, A) => Int): MyListHOF[A]

  def zipWith[B, C](list: MyListHOF[B], zip: (A, B) => C): MyListHOF[C]

  def fold[B](start: B)(operator: (B, A) => B): B
}

case object EmptyHOF extends MyListHOF[Nothing] {
  def getHead: Nothing = throw new NoSuchElementException

  def getTail: MyListHOF[Nothing] = throw new NoSuchElementException

  def isEmpty: Boolean = true

  def add[B >: Nothing](n: B): MyListHOF[B] = new ConsHOF(n, EmptyHOF)

  def printNodes: String = ""

  def map[B](transformer: (Nothing => B)): MyListHOF[B] = EmptyHOF

  def filter(predicate: (Nothing => Boolean)): MyListHOF[Nothing] = EmptyHOF

  def flatMap[B](transformer: (Nothing => MyListHOF[B])): MyListHOF[B] = EmptyHOF

  def ++[B >: Nothing](list: MyListHOF[B]): MyListHOF[B] = list

  def foreach(f: Nothing => Unit): Unit = ()
  
  def sort(compare: (Nothing, Nothing) => Int): MyListHOF[Nothing] = EmptyHOF

  def zipWith[B, C](list: MyListHOF[B], zip: (Nothing, B) => C): MyListHOF[C] = {
    if(!list.isEmpty) throw new RuntimeException("Lists do not have the same length")
    else EmptyHOF
  }

  def fold[B](start: B)(operator: (B, Nothing) => B): B = start
}

case class ConsHOF[+A](head: A, tail: MyListHOF[A]) extends MyListHOF[A] {
  def getHead: A = head

  def getTail: MyListHOF[A] = tail

  def isEmpty: Boolean = false

  def add[B >: A](n: B): MyListHOF[B] = new ConsHOF(n, this)

  def printNodes: String = {
    if(tail.isEmpty) "" + head
    else head + "->" + tail.printNodes
  }

  def filter(predicate: (A => Boolean)): MyListHOF[A] = {
    if(predicate(head)) new ConsHOF(head, tail.filter(predicate))
    else tail.filter(predicate)
  }

  def map[B](transformer: (A => B)): MyListHOF[B] = {
    new ConsHOF[B](transformer(head), tail.map(transformer))
  }

  def flatMap[B](transformer: (A => MyListHOF[B])): MyListHOF[B] = {
    transformer(head) ++ tail.flatMap(transformer)
  }

  def ++[B >: A](list: MyListHOF[B]): MyListHOF[B] = new ConsHOF[B](head, tail ++ list)

  def foreach(f: A => Unit): Unit = {
    f(head)
    tail.foreach(f)
  }

  def sort(compare: (A, A) => Int): MyListHOF[A] = {
    def insert(x: A, sortedList: MyListHOF[A]): MyListHOF[A] = {
      if(sortedList.isEmpty) new ConsHOF(x, EmptyHOF)
      else if(compare(x, sortedList.getHead) <= 0) new ConsHOF(x, sortedList)
      else new ConsHOF(sortedList.getHead, insert(x, sortedList.getTail))
    }

    val sortedTail = tail.sort(compare)
    insert(head, sortedTail)
  }

  def zipWith[B, C](list: MyListHOF[B], zip: (A, B) => C): MyListHOF[C] = {
    if(list.isEmpty) throw new RuntimeException("Lists do not have the same length")
    else new ConsHOF(zip(head, list.getHead), tail.zipWith(list.getTail, zip))
  }

  def fold[B](start: B)(operator: (B, A) => B): B = {
    tail.fold(operator(start, head))(operator)

    /*
    * [1, 2, 3].fold(0)(+)
    * = [2, 3].fold(operator(0, 1) -> 0 + 1 = 1)(+)
    * = [3].fold(operator(1, 2) -> 1 + 2 = 3)(+)
    * = // EmptyHOF [].fold(operator(3, 3) -> 3 + 3 = 6)(+)
    * = 6
    * */
  }
}

object MyListHOFTest extends App {
  val l1: MyListHOF[String] = new ConsHOF("Hello", new ConsHOF("Scala", EmptyHOF))
  val clonel1: MyListHOF[String] = new ConsHOF("Hello", new ConsHOF("Scala", EmptyHOF))
  println(l1.toString)
  println(clonel1 == l1)

  val l2: MyListHOF[Int] = new ConsHOF(1, new ConsHOF(2, new ConsHOF(3, EmptyHOF)))
  println(l2.toString)

  println(l2.map(x => x * 2))

  println(l2.filter(n => n % 2 == 0))

  val l3: MyListHOF[Int] = new ConsHOF(4, new ConsHOF(5, EmptyHOF))
  println(l3.toString)

  println(l2 ++ l3)
  println(l2.flatMap(n => new ConsHOF(n, new ConsHOF(n + 1, EmptyHOF)) ))

  l2.foreach(println) // same as l2.foreach(x => println(x))

  val l4: MyListHOF[Int] = new ConsHOF(5, new ConsHOF(3, new ConsHOF(4, EmptyHOF)))
  println(l4.sort((x, y) => y - x))

  println(l3.zipWith[String, String](l1, _ + " " +_))

  println(l4.fold(0)(_ + _))


  // for comprehensions

  val combo = for {
    n <- l2
    str <- l1
  } yield n + "-" + str

  println(combo)
}
