package lectures.part5_adv_scala

import scala.util.Try

object SyntaxSugarAdv extends App {

  // SS#1: methods with single parameter
  def singleArgMethod(arg: Int): String = s"$arg little ducks"
  val desc = singleArgMethod {
    // code
    42
  }

  val aTryInstance = Try { // ~ java's Try{...}
    throw new RuntimeException
  }

  List(1, 2, 3).map { x =>
    x + 1
  }

  // SS#2: instances of traits with a single method can actually be reduced to a lambda
  // aka single abstract method pattern

  trait Action {
    def act(x: Int): Int
  }

//  val anInstance: Action = new Action {
//    override def act(x: Int): Int = x + 1
//  }

  // the above can be reduced to
  val anInstance: Action = (x: Int) => x + 1

  // example: Runnable
  val aThread = new Thread(new Runnable {
    override def run(): Unit = println("hello, Scala")
  })

  val scalaThread = new Thread(() => println("hello Scala Syntactic Sugar"))

  abstract class AbstractType {
    def implemented: Int = 23
    def f(a: Int): Unit
  }

  val absInst: AbstractType = (a: Int) => println(s"$a sweet")

  // SS#3: the :: and #:: method
  val prepList = 2 :: List(3, 4)
  // with the current knowledge, infix methods are first converted to 2.::(List(3, 4))
  // but in actuality, the compiler rewrites this as List(3, 4).::(2)
  // Scala Specification: the associativity of a method is determined by the operator's last character
  // if the operator ends with a colon, then it's RIGHT ASSOCIATIVE  else it's LEFT ASSOCIATIVE
  1 :: 2 :: 3 :: List(4, 5)
  // first 3 :: List(4, 5) is evaluated then
  // 2 :: (3 :: List(4, 5)) then
  // 1 :: (2 :: (3 :: List(4, 5)))
  // compiler -> List(4, 5).::(3).::(2).::(1)

  class MyStream[T] {
    def -->:(value: T): MyStream[T] = this // implementation

    val myStream = 1 -->: 2 -->: 3 -->: new MyStream[Int] // this statement is valid because
    // the arrow-colon operator is RIGHT ASSOCIATIVE as it ends in a colon
    // in retrospect, :: and #:: end in a colon as well and as a result are RIGHT ASSOCIATIVE
  }

  // SS#4: multi-word method naming
  class TeenGirl(name: String) {
    def `and then said` (gossip: String): Unit = println(s"$name said $gossip")
  }

  val lily = new TeenGirl("Lily")
  lily `and then said` "Scala has crazy SS"

  // SS#5: infix types
  class Composite[A, B]
  val composite1: Composite[Int, String] = new Composite[Int, String]
  val composite2: Int Composite String = new Composite[Int, String] // same as the above line (infix types syntactic sugar)

  class -->[A, B]
  val towards: Int --> String = new -->[Int, String]

  // SS#6: update() is a special method like apply()
  val ar = Array(1, 2, 3)
  ar(2) = 7 // rewritten to ar.update(index = 2, value to put in = 7)
  // used in mutable collections

  // SS#7: setters for mutable containers
  class Mutable {
    private var internalMember: Int = 0 // private for OO encapsulation
    def member: Int = internalMember
    def member_=(value: Int): Unit = {
      internalMember = value
    }

    val mutableContainer = new Mutable
    mutableContainer.member = 42 // rewritten as mutableContainer.member_=(42)
    // this SS only happens if we declare a 'getter' and 'setter' called member
  }
}
