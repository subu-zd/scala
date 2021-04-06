package exercises

/*
* 1.
* Create a Generic trait - MyPredicate[T]
* it will have a small method to test whether a value of type T passes a condition (every subclass will have a different implementation)
* test(T) => return Boolean
*
* for e.g. we have a concrete class
* class EvenPredicate extends MyPredicate[Int]
*
* that test method will take an Int as a parameter and will return if the parameter was EVEN or NOT
*
* 2.
* Generic trait - MyTransformer[A, B]
* it will have method to convert a value of type A into a value of type B (every subclass will have a different implementation)
* transform(A) => B
*
* for e.g.
* class StringToIntTransformer extends MyTransformer[String, Int]
*
* the transform method inside will receive a parameter of type A and returns a value of type B
* so in this case, it will transform a String into Int
*
* functions of MyList
* - map(MyTransformer) => new MyList (of different type)
* - filter(MyPredicate) => new MyList
* - flatMap (MyTransformer from A to MyList[B]) => MyList[B]
*
* # map
*   lets assume you have the following list:
*   [1, 2, 3].map(n * 2) => [2, 4, 6]
*
* # filter
*   [1, 2, 3, 4].filter(n % 2) = [2, 4]
*
* # flatmap
*   [1, 2, 3].flatMap(n => [n, n + 1]) => [1, 2, 2, 3, 3, 4]
*
* */

abstract class GenList2[+A] {

  def getHead: A

  def getTail: GenList2[A]

  def isEmpty: Boolean

  def add[B >: A](n: B): GenList2[B]

  def printNodes: String

  override def toString: String = "[" + printNodes + "]"

  def map[B](transformer: MyTransformer[A, B]): GenList2[B]

  def filter(predicate: MyPredicate[A]): GenList2[A]

  def flatMap[B](transformer: MyTransformer[A, GenList2[B]]): GenList2[B]

  def ++[B >: A](list: GenList2[B]): GenList2[B]
}

object EmptyGen2 extends GenList2[Nothing] {
  def getHead: Nothing = throw new NoSuchElementException

  def getTail: GenList2[Nothing] = throw new NoSuchElementException

  def isEmpty: Boolean = true

  def add[B >: Nothing](n: B): GenList2[B] = new ConsGen2(n, EmptyGen2)

  def printNodes: String = ""

  def map[B](transformer: MyTransformer[Nothing, B]): GenList2[B] = EmptyGen2

  def filter(predicate: MyPredicate[Nothing]): GenList2[Nothing] = EmptyGen2

  def flatMap[B](transformer: MyTransformer[Nothing, GenList2[B]]): GenList2[B] = EmptyGen2

  def ++[B >: Nothing](list: GenList2[B]): GenList2[B] = list
}

class ConsGen2[+A](head: A, tail: GenList2[A]) extends GenList2[A] {
  def getHead: A = head

  def getTail: GenList2[A] = tail

  def isEmpty: Boolean = false

  def add[B >: A](n: B): GenList2[B] = new ConsGen2(n, this)

  def printNodes: String = {
    if(tail.isEmpty) "" + head
    else head + "->" + tail.printNodes
  }

  def filter(predicate: MyPredicate[A]): GenList2[A] = {
    if(predicate.test(head)) new ConsGen2(head, tail.filter(predicate))
    else tail.filter(predicate)
  }


  def map[B](transformer: MyTransformer[A, B]): GenList2[B] = {
    new ConsGen2[B](transformer.transform(head), tail.map(transformer))
  }

  def flatMap[B](transformer: MyTransformer[A, GenList2[B]]): GenList2[B] = {
    transformer.transform(head) ++ tail.flatMap(transformer)
  }

  def ++[B >: A](list: GenList2[B]): GenList2[B] = new ConsGen2[B](head, tail ++ list)
}

trait MyPredicate[-T] {
  def test(elem: T): Boolean
}

trait MyTransformer[-A, B] {
  def transform(elem: A): B
}

object GenListTest2 extends App {
  val l1: GenList2[String] = new ConsGen2("Hello", new ConsGen2("Scala", EmptyGen2))
  println(l1.toString)

  val l2: GenList2[Int] = new ConsGen2(1, new ConsGen2(2, new ConsGen2(3, EmptyGen2)))
  println(l2.toString)

  println(l2.map(new MyTransformer[Int, Int] {
    override def transform(n: Int): Int = n * 2
  }))

  println(l2.filter(new MyPredicate[Int] {
    override def test(n: Int): Boolean = n % 2 == 0
  }))

  val l3: GenList2[Int] = new ConsGen2(4, new ConsGen2(5, EmptyGen2))
  println(l3.toString)

  println(l2 ++ l3)
  println(l2.flatMap(new MyTransformer[Int, GenList2[Int]] {
    override def transform(n: Int): GenList2[Int] = new ConsGen2(n, new ConsGen2(n + 1, EmptyGen2))
  }))
}


