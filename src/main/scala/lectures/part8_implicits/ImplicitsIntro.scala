package lectures.part8_implicits

object ImplicitsIntro extends App {

  val pair = "Daniel" -> "555"
  /*
   * The current definition of "operators"
   * - operators act as methods
   * - in which the first operand (in this case "Daniel") is the instance who is calling the method
   * - and the other operand is the argument (in this case "555")
   *
   * In this above example however, String class doesn't have an "Arrow ->" method
   * and in actuality, this "Arrow ->" method belongs to an implicit ArrowAssoc class
   *
   * The "implicit" keyword turns the first argument (in this case "Daniel") to an ArrowAssoc instance
   * and then it will call the "Arrow ->" method on it with the argument (in this case "555")
   * and then it will return a tuple
   * */

  case class Person(name: String) {
    def greet = s"Hi! my name is $name"
  }

  implicit def fromStringToPerson(str: String): Person = Person(str)

  println("Peter".greet)

  /* In the above println statement, the String class doesn't natively have a "greet" method
   * but the compiler looks for all implicit classes, objects, values and methods that can help in the compilation
   * i.e. it looks for anything that cant turn this string into something that has a greet method
   *
   * it just so happens that there is a person type that has a "greet" method and
   * an implicit conversion from a string to the type "Person" which has a "greet" method
   *
   * Having matches, the compiler wraps and re-writes this code into :
   * println(fromStringToPerson("Peter").greet)
   *
   * NOTE: in the above example, the compiler assumes that there is only one implicit that matches
   * if there are multiple implicits that match, then the code will not compile
   *
   * E.g.
   * a different ...
   * class A {
   *   def greet: Int = 42
   * }
   *
   * implicit def fromStringToA(str: String): A = new A // THIS WON'T COMPILE
   * */

  // implicit parameters
  def increment(x: Int)(implicit amount: Int) = x + amount

  implicit val defaultAmount = 10

  println(increment(2)) // the defaultAmount will be implicitly passed by the compiler as the second parameter list
  // we can also override the implicit arg and use the second parameter list if desired
  // NOTE: this is NOT the same thing as default arguments because the default value is FOUND BY THE COMPILER from the its SEARCH SCOPE.
  // the above example is how FUTURES were constructed with an implicit parameter list
}
