package exercises

abstract class MyList {

  /*
  * This list will be singly linked list which holds integers
  *
  * methods
  *
  * 1. head - first element of the list
  * 2. tail - remainder of the list
  * 3. isEmpty - return boolean (is this empty?)
  * 4. add(int) - new list with this element(int) added
  * 5. toString - a string representation of the list
  * */
  def getHead: Int

  def getTail: MyList

  def isEmpty: Boolean

  def add(n: Int): MyList

  def printNodes: String

  override def toString: String = "[" + printNodes + "]"
}

object Empty extends MyList {
  def getHead: Int = throw new NoSuchElementException // throw exceptions are exceptions that return the scala.Nothing type

  def getTail: MyList = throw new NoSuchElementException

  def isEmpty: Boolean = true

  def add(n: Int): MyList = new Cons(n, Empty)

  def printNodes: String = ""
}

class Cons(head: Int, tail: MyList) extends MyList {
  def getHead: Int = head

  def getTail: MyList = tail

  def isEmpty: Boolean = false

  def add(n: Int): MyList = new Cons(n, this)

  def printNodes: String = {
    if(tail.isEmpty) "" + head
    else head + "->" + tail.printNodes
  }
}

object ListTest extends App {
  val l1 = new Cons(1, Empty)
  println(l1.getHead)

  val l2 = new Cons(1, new Cons(2, new Cons(3, Empty)))
  println(l2.getTail.getHead)
  println(l2.add(4).getHead)
  println(l2.isEmpty)

  // this toString will go to the toString method in the abstract class which calls the printNodes method
  // which is then derived by the 2 implementations - Empty & Cons
  // this allows the appropriate printNodes element to be called later which is a Polymorphic call
  println(l2.toString)
}
