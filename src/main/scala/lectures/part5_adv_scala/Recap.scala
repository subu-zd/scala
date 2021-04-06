package lectures.part5_adv_scala

import scala.annotation.tailrec

object Recap extends App {

  val aCondition: Boolean = false
  val aConditionVal = if(aCondition) 42 else 65
  // instructions vs expressions

  // compiler infers the type automatically
  val aCodeBlock = {
    if (aCondition) 54
    56
  }

  // Unit - (~ void) do not return anything but only do side-effects for e.g. printing to the console, modifying a variable etc.
  val theUnit = println("Hello Scala")

  // functions
  def aFunc(x: Int): Int = x + 1

  // recursion: stack and tail
  @tailrec
  def fact(n: Int, acc: Int): Int = {
    if(n <= 0) acc
    else fact(n - 1, n * acc)
  }

  // OOP
  class Animal
  class Dog extends Animal
  val aDog: Animal = new Dog // subtyping polymorphism

  trait Carnivore {
    def eat(a: Animal): Unit
  }

  class Crocodile extends Animal with Carnivore {
    override def eat(a: Animal): Unit = println("crunch!")
  }

  // method notation
  val aCroc = new Crocodile
  aCroc eat aDog // natural language

  // anonymous classes
  val aCarnivore = new Carnivore {
    override def eat(a: Animal): Unit = println("roar")
  }

  // generics
  abstract class MyListRecap[+A] // variance

  // singleton objects and companions
  object MyListRecap

  // case classes
  case class Person(name: String, age: Int)
  // "case class" implies that:
  // 1. class Person is serializable
  // 2. all parameters are fields that have companion objects with apply methods

  // Exceptions and try/catch/finally
  val throwsException = throw new RuntimeException // Nothing (has not instances)
  val potentialFailure = try {
    throw new RuntimeException
  } catch {
    case e: Exception => "I caught an exception"
  } finally {
    println("logs")
  }

  // packaging and imports

  // functional programming
  val inc = new Function[Int, Int] {
    override def apply(v1: Int): Int = v1 + 1
  }

  inc(1)

  // anonymous function
  val aInc = (x: Int) => x + 1
  List(1, 2, 3).map(aInc) // "map" in this case is a Higher Order Function

  // for-comprehensions
  val pairs = for {
    num <- List(1, 2, 3)
    char <- List('a', 'b', 'c')
  } yield num + " " + char

  // Scala collections: Seq, Array, List, Vector, Map, Tuple
  val aMap = Map(
    "Daniel" -> 789,
    "Jess" -> 555
  )

  // Option, Try
  val anOption = Some(2)

  // pattern matching
  val x = 2
  val order = x match {
    case 1 => "First"
    case 2 => "Second"
    case 3 => "Third"
    case _ => s"${x}th"
  }

  val bob = Person("Bob", 2)
  val greeting = bob match {
    case Person(name, _) => s"Hi! my name is $name"
  }
}
