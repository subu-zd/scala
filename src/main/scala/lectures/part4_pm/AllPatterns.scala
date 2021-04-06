package lectures.part4_pm

import exercises.{ConsCC, EmptyCC, MyListCC}

object AllPatterns extends App {
  // 1 - constants (background for the enhanced switch case)
//  val x: Any = "Scala"
//  val constants = x match {
//    case 1 => "a number"
//    case "Scala" => "THE SCALA"
//    case true => "Truth"
//    case AllPatterns => "a singleton object"
//  }
//
//  // 2 - match anything
//  // 2.1 wildcard
//  val matchAnything = x match {
//    case _ => "anything"
//  }
//
//  // 2.2 variable
//  val matchVariable = x match {
//    case something => s"I've found $something"
//  }
//
//  // 3 - Tuples
//  val aTuple = (1, 2)
//  val matchTuple = aTuple match {
//    case (1, 1) =>
//    case (something, 2) => s"I've found $something" // this case will try to extract "something" out of tuple if the rest of pattern matches
//  }
//
//  // Pattern Matching can be nested
//  val nestedTuple = (1, (2, 3))
//  val matchNested = nestedTuple match {
//    case (_, (2, v)) => ""
//  }
//
//  // 4 - Case Classes - Constructor Pattern
//  val aList: MyListCC[Int] = ConsCC(1, ConsCC(2, EmptyCC))
//  val matchList = aList match {
//    case EmptyCC => ""
//    case ConsCC(head, tail) => ""
//    case ConsCC(head, ConsCC(subhead, subtail)) => ""
//  }
//
//  // 5 - list patterns
//  val anotherList = List(1, 2, 3, 42)
//  val listMatching = anotherList match {
//    case List(1, _, _, _) => ""// thia case is called an EXTRACTOR for a list. even though List is NOT a Case Class
//    case List(1, _*) => ""// a list of arbitrary length
//    case 1 :: List(_) => ""// infix pattern
//    case List(1, 2, 3) :+ 42 => ""// infix pattern
//  }
//
//  // 6 - type specifiers
//  val unknown: Any = 2
//  val unknownMatch = unknown match {
//    case list: List[Int] => ""// explicit type specifier
//    case _ => ""
//  }
//
//  // 7 - name binding
//  val nameBinding = aList match {
//    case nonEmptyList @ ConsCC(_,_) => ""// name binding  => acts as an alias which allows you to use the name later (or here)
//    case ConsCC(1, rest @ ConsCC(2, _)) => ""// name binding inside nested patterns
//  }
//
//  // 8 - multi-patterns
//  val multiPattern = aList match {
//    case EmptyCC | ConsCC(0, _) => ""// compound pattern or multi-pattern. You can chain more patterns using the "|" operator if you want to return the same result for multiple patterns
//  }
//
//  // 9 - IF GUARDS
//  val secondElementSpecial = aList match {
//    case ConsCC(_, ConsCC(specialElement, _)) if specialElement % 2 == 0 => ""
//  }

  val nos = List(1, 2, 3)
  val nosMatch = nos match {
    case listOfStrings: List[String] => "a list of Strings"
    case listOfNumber: List[Int] => "a list of nos"
    case _ => ""
  }

  println(nosMatch) // this prints out "a list of Strings" i.e. the first case
  // it's due to the JVM
  // JVM was designed while keeping backwards compatibility in mind
  // e.g. a Java 9 JVM can run Java 1 programs
  // the problem is that Generics (i.e. type parameters) were introduced in Java 5
  // and to make the JVM compatible with Java 1, the Java 5 compiler erased all generic types after type checking which makes the JVM oblivious to generic types.


  // so in this case, after type checking, the pattern matching expression looks like this
  /*
  * val nosMatch = nos match {
      case listOfStrings: List => "a list of Strings"
      case listOfNumber: List => "a list of nos"
      case _ => ""
    }
  *
  * */
  // SCALA COMPILER WARNING => fruitless type test: a value of type List[Int] cannot also be a List[String](but still might match its erasure)
}

