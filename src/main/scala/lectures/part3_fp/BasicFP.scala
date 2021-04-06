package lectures.part3_fp

object BasicFP extends App {
  val doubler = new MyFunction[Int, Int] {
    override def apply(e: Int): Int = e * 2
  }

  println(doubler(2)) // by using the "method, we can call the instance like a function
  // "doubler" which is an instance of a function like class can be called like a function

  // NOTE: Scala supports these function types out-of-the-box
  // the function types are Function1, Function2, Function3 .... Function22"
  // meaning that by-default Scala supports these function types up-to 22 parameters

  // a function with 1 parameter and 1 result is the Function1[A, B]
  // this is a function type by default supported in Scala

  // val stringToIntConverter = new Function1[String, Int]. below is the syntactic sugar
  val stringToIntConverter = new (String => Int) {
    override def apply(str: String): Int = str.toInt
  }

  println(stringToIntConverter("3") + 4)

  val adder = new ((Int, Int) => Int) {
    override def apply(a: Int, b: Int): Int = a + b
  }

  // NOTE: All Scala functions are objects or instances of classes deriving from Function1, Function2 .... etc

  /* Exercises
  * 1. a function which takes 2 strings and concatenates them
  * 2. transform the MyPredicate and MyTransform into function types
  * 3. define a function which takes an int and returns another function which takes an int and returns an int
  *   - define what's the type of this function
  *   - how to do it?
  * */

  // Q1.

  val stringConcat = new ((String, String) => String) {
    override def apply(s1: String, s2: String): String = s1 + s2
  }

  println(stringConcat("Hello ", "World"))

//  val superAdder: Function1[Int, Function1[Int, Int]] = new Function1[Int, Function1[Int, Int]] {
//    override def apply(x: Int): Function1[Int, Int] = new Function1[Int, Int] {
//      override def apply(y: Int): Int = x + y
//    }
//  }

  // using syntactic sugar
  val superAdder: (Int => (Int => Int)) = new (Int => (Int => Int)) {
    override def apply(x: Int): (Int => Int) = new (Int => Int) {
      override def apply(y: Int): Int = x + y
    }
  }

  val adder3 = superAdder(3)
  println(adder3(4))
  println(superAdder(3)(4)) // same as the line above
  // this type of function is also called CURRIED FUNCTION
}

// we would write something like this i.e. classes and methods
// and the way we would use this as "functions" would be to instantiate this class either anonymously or non-anonymously
// you would simulate functions similar to that done in MyList Predicate
trait MyFunction[A, B] {
  def apply(e: A): B = ???
}
