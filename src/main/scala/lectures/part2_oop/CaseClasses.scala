package lectures.part2_oop

object CaseClasses extends App {

  case class Person(name: String, age: Int)

  // 1. class parameters are promoted to fields
  val jim = new Person("Jim", 34)
  println(jim.name)

  // 2. a very sensible variant of toString which helps in debugging
  // println(jim) is a syntactic sugar that automatically delegates the instance jim to printed as toString
  println(jim.toString)
  // outputs Person(Jim, 34) unlike a cryptic hashcode

  // 3. equals and hashCode implemented Out-of-the-Box
  val jim2 = new Person("Jim", 34)
  println(jim == jim2)
  // with case classes, the above print statement return "true"
  // if the "case" keyword hadn't been used here, the code would've returned "false"
  // because jim and jim2 are two different instances of class Person but the "equals" method was not implemented,
  // so the default from AnyRef was picked which by default for different it returns false

  // 4. Handy Copy methods
  val jim3 = jim.copy() // creates a new instance of case class
  // it also receives name parameters and the specified parameter is updated in the new instance
  val jim4 = jim.copy(age = 45)
  println(jim4)

  // 5. Case classes have companion objects
  val thePerson = Person // automatically creates a companion object
  val mary = thePerson("Mary", 23) // using the apply method (makes a companion object callable like a function) and passing parameters to create a Person instance
  // this delegates to the Person's apply() method
  // so the companion's object apply() method does the same thing as a constructor
  println(mary)

  // 6. Case Classes are serializable
  // this makes classes especially useful when dealing with distributed systems
  // this allows you to send instances of case classes through the network and in between JVM
  // this is especially important when dealing for e.g. in the AKKA framework (AKKA deals with sending serializable messages through the network)
  // messages, in-general in practice, are case classes

  // 7. Case classes have extractor patterns. This implies that CCs can be used in PATTERN MATCHING

  // CASE OBJECTS have the same properties as CASE CLASSES except they don't get companion objects
  // because they are their own companion objects
  case object UnitedKingdom {
    def name: String = "The UK of GB and NIE"
  }
}
