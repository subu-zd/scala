package lectures.part4_pm

import scala.util.Random

object PatternMatching extends App {
  // similar to an enhanced switch-case
  val random = new Random()
  val x = random.nextInt(10)

  val description = x match {
    case 1 => "the ONE"
    case 2 => "2"
    case 3 => "three"
    case _ => "anything" // the underscore is known as the WILDCARD
  }

  println(x)
  println(description)

  // 1. Decompose values
  case class Person(name: String, age: Int)
  val bob = Person("Bob", 20)
  val abc = "wd"

  val greeting = bob match {
    case Person(n, a) if a < 21 => s"Hi! my name is $n and I can't drink in the US"
    case Person(n, a) => s"Hi! my name is $n and I am $a years old"
    case _ => "I don't know who I am"
  }

  println(greeting)
  // 1. Cases are matched in order and the first pattern that matches will return the appropriate operation
  // 2. when no cases match, the scala compiler throws a MatchError. This makes it imperative to use Wildcards while Pattern Matching
  // 3. return type: if all the cases return the same data type, then Pattern Matching data type will be the same as well.
  //    Otherwise, the compiler will unify the types and return the lowest common ancestor of all the types returned by all the cases
  // 4. Cases classes work very well with PM as they come with extractor patterns out of the box. (unlike normal classes. for normal classes you can't use them for pattern matching)

  // NOTE: Avoid using pattern matching or complicated code for implementing simple logic

  // Pattern Matching on sealed hierarchies (results into a compiler warning)
  sealed class Animal
  case class Dog(breed: String) extends Animal
  case class Parrot(greeting: String) extends Animal

  val animal: Animal = Dog("Beagle")
  animal match {
    case Dog(someBreed) => println(s"$someBreed found")
  }

  // By not including a case for "Parrot" or a wildcard case, the compiler issues a warning that "match may not be exhaustive"
  // this is because the "Animal" class is SEALED

  /* EXERCISE
  * 1. a method that uses PM
  *   - takes an Expr as a param and returns a human readable form of it.
  *     e.g. Sum(Number(2), Number(3)) => 2 + 3
  *          Sum(Number(2), Number(3), Number(4)) => 2 + 3 + 4
  *          Prod(Sum(Number(2), Number(1)), Number(3)) => (2 + 1) * 3
  *          Sum(Prod(Number(2), Number(1)), Number(3)) => (2 + 1) * 3
  *
  * */

  trait Expr
  case class Number(n: Int) extends Expr
  case class Sum(e1: Expr, e2: Expr) extends Expr
  case class Prod(e1: Expr, e2: Expr) extends Expr

  def show(e: Expr): String = e match {
    case Number(n) => s"$n"
    case Sum(e1, e2) => show(e1) + " + " + show(e2)
    case Prod(e1, e2) =>
      def maybeShowParenthesis(exp: Expr): String = exp match {
        case Prod(_, _) => show(exp)
        case Number(_) => show(exp)
        case _ => "(" + show(exp) + ")"
      }
      maybeShowParenthesis(e1) + " * " + maybeShowParenthesis(e2)
  }

  println(show(Sum(Number(2), Number(3))))
  println(show(Sum(Sum(Number(2), Number(3)), Number(4))))
  println(show(Prod(Sum(Number(2), Number(1)), Number(3))))
  println(show(Sum(Prod(Number(2), Number(1)), Number(3))))
}
