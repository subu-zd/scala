package lectures.part6_adv_fp

object PartialFunctions extends App {
  val aFunc = (x: Int) => x + 1 // Function1[Int, Int] === Int => Int

  val aFussyFunction = (x: Int) =>
    if (x == 1) 42
    else if (x == 2) 56
    else if (x == 5) 999
    else throw new FunctionNotApplicableException

  class FunctionNotApplicableException extends RuntimeException

  val aNicerFF = (x: Int) =>
    x match {
      case 1 => 42
      case 2 => 56
      case 5 => 999
    }
  // The above is actually an implementation of Int Subset {1, 2, 5} => Int
  // Hence, this function is also called a PARTIAL FUNCTION (as it only accepts a part of the Int domain as arguments)

  val aPartialFunc: PartialFunction[Int, Int] = {
    case 1 => 42
    case 2 => 56
    case 5 => 999
  } // a partial function value (supported by Scala natively and is equivalent to the above value)

  println(aPartialFunc(5))
  // a value outside the Int domain subset i.e. {1, 2, 5} will crash the PartialFunction with a MatchError
  // as it is based on pattern matching

  // Partial Function Utilities
  println(aPartialFunc.isDefinedAt(67)) // isDefinedAt() tests if the argument supplied is applicable for the Partial function
  // it helps to check partial functions without applying them and crashing the program if the arg is not valid

  // partial functions can be "LIFTed" to total functions returning Option[]
  // so that if a partial function is not defined for an argument, it will return None

  val lifted = aPartialFunc.lift // turns aPartialFunc to total function of type Int => Option[Int]
  println(lifted(2))
  println(lifted(98))

  val pfChain = aPartialFunc.orElse[Int, Int] { case 45 =>
    67
  }

  println(pfChain(2))
  println(pfChain(45))

  // Partial Functions extend Normal Functions
  val aFunction: Int => Int = { case 1 =>
    99
  }

  // HOFs accept partial functions as well
  val aMappedList = List(1, 2, 3).map {
    case 1 => 42
    case 2 => 78
    case 3 => 1000
  }

  println(aMappedList)

  // Note: Unlike functions which can have multiple parameter types, a Partial function can only have one parameter type

  /* EXERCISE
   *
   * 1. construct a partial function instance (anonymous class)
   * 2. chat bot as a partial function
   * */

  val aManualFF = new PartialFunction[Int, Int] {
    override def apply(v1: Int): Int = v1 match {
      case 1 => 42
      case 2 => 65
      case 5 => 999
    }

    override def isDefinedAt(x: Int): Boolean = {
      x == 1 || x == 2 || x == 5
    }
  }

  val chatbot: PartialFunction[String, String] = {
    case "hello"    => "Hi, my name is HAL9000"
    case "goodbye"  => "Once you start talking to me, there is no return, human"
    case "call now" => "Unable to find your phone without your credit card"
    case _          => "Umm! Not sure how to respond to that "
  }

  scala.io.Source.stdin.getLines().foreach(line => println(s"you said: ${chatbot(line)}"))
}
