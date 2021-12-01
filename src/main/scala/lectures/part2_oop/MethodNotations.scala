package lectures.part2_oop

import scala.language.postfixOps

object MethodNotations extends App {
  class Person(val name: String, favMovie: String, val age: Int = 0) {

    def likes(movie: String): Boolean = movie == favMovie

    def hangsOutWith(person: Person): String = s"${this.name} is hanging out with ${person.name}"

    def unary_! : String = s"$name What the heck?!"

    def isAlive: Boolean = true

    def apply(): String = s"Hi, my name is $name and I like $favMovie"

    // EXERCISE methods

    def +(nickName: String): Person = new Person(s"$name (the $nickName)", favMovie)

    def unary_+ : Person = new Person(name, favMovie, age + 1)

    def learns(str: String): String = s"$name learns $str"

    def learnsScala: String = this learns "Scala"

    def apply(n: Int): String = s"$name watched $favMovie $n times"
  }

  val mary = new Person("Mary", "Inception")
  println(mary.likes("Inception"))
  println(mary likes "Inception") // equivalent
  // Infix notation / operator notation (Example of syntactic sugar)
  // only works with methods with one parameter

  // "Operators" in Scala
  val tom = new Person("Tom", "Fight Club")
  println(mary hangsOutWith tom)
  // in this case, the method "hangOutWith()" acts like an operator yielding a String
  // Scala has an extremely permissive naming scheme (allows function names to be chars like "+", "&" etc)

  // In Scala, all operators are methods.
  // E.g. println(1 + 2) == println(1.+(2))

  // Prefix Notation
  // prefix notation is all about unary operators!
  // in Scala, unary operators are also methods with unary_ prefix.
  // it only works with a few operators +, -, ~, !

  val x: Int = -1
  val y: Int = -(-1)

  println(x == y)
  println(!mary)
  println(!mary)

  // Postfix Notation
  // ONLY functions that do not receive any parameters have the property to be used in a postfix notation
  // it is rarely used in practice as it can introduce potential ambiguity while reviewing code

  println(mary.isAlive)
  println(mary isAlive) // Postfix notation

  // special method: apply

  // whenever the compiler sees an object called like a method, it looks for the definition of an "apply()" method in the particular class
  // this specific method (apply()) breaks the barrier between object oriented programming and functional programming
  println(mary.apply())
  println(mary()) // equivalent

  // EXERCISES

  /*
  * 1. Overload the + operator
  *     mary + "the rockstar" => new person "Mary (the rockstar)"
  *
  * 2. Add an age to the Person class (default val = 0)
  *     Add a unary + operator => new person with the age + 1
  *     +mary => mary with the age incremented
  *
  * 3. Add a "learns" method in the Person class => "Mary learns Scala"
  *     Add a learnsScala method, calls learns method with "Scala"
  *     Use it in postfix notation
  *
  * 4. Overload the apply method to receive a number and return a string
  *     mary.apply(2) => "Mary watched Inception 2 times"
  * */

  println((mary + "rockstar")())
  println((+mary).age)
  println(mary learnsScala)
  println(mary(2))


}
