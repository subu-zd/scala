package lectures.part5_adv_scala

object AdvPatternMatching extends App {
  val nos = List(1)
  val description = nos match {
    case head :: Nil => println(s"the only element is $head")
    case _ =>
  }

  /*
  * constants
  * wildcards
  * case classes
  * tuples
  * example like above i.e. 'description'
  */

  // how to make classes (which are not case classes) compatible with pattern matching
  class Person(val name: String, val age: Int)

  // first step: define a companion object
  object Person {
    // in the companion object, we define a special method "unapply()"
    // with a return type Option that should be of the type of result you want to decompose
    def unapply(person: Person): Option[(String, Int)] = {
      if(person.age < 21) None
      else Some((person.name, person.age))
    }

    // we can overload the unapply method
    def unapply(age: Int): Option[String] =
      Some(if (age < 21) "minor" else "major")
  }

  val bob = new Person(name = "Bob", age = 25)
  val greeting = bob match {
    case Person(n, a) => s"Hi my name is $n and I am $a years old" // this pattern is not associated to the Person class
    // but is associated to the Person singleton object which also acts as a companion object to the Person class
  }

  println(greeting)

  val legalStatus = bob.age match {
    case Person(status) => s"My legal status is $status"
  }

  println(legalStatus)

  /*
  * Exercise
  * */

  val n: Int = 7
  val mathProp = n match {
    case singleDigit(_) => "single digit"
    case even(_) => "even number"
    case _ => "no property"
  }

  object even {
    def unapply(arg: Int): Option[Boolean] =
      if(arg % 2 == 0) Some(true)
      else None
  }

  object singleDigit {
    def unapply(arg: Int): Option[Boolean] =
      if(arg > -10 && arg < 10) Some(true)
      else None
  }

  // alternative way
  /*
  *
  object singleDigit {
    def unapply(arg: Int): Boolean = arg > -10 && arg < 10
  }
  *
  object even {
    def unapply(arg: Int): Boolean = arg % 2 == 0
  }
  *
  val mathProp = n match {
    case singleDigit() => "single digit"
    case even() => "even number"
    case _ => "no property"
  }
  *
  * */

  println(mathProp)

  // infix patterns
  case class Or[A, B](a: A, b: B) // this type is aka Either (similar to Option[])
  val either = Or(2, "two")
  val humanDesc = either match {
//    case Or(no, str) => s"$no is written as $str"
    case no Or str => s"$no is written as $str" // same as above
  }

  println(humanDesc)

  // decomposing sequences
  val vararg = nos match {
    case List(1, _*) => "starting with 1" // this is a pattern match against the whole list as a sequence
      // it may have 1 or 2 or 3 or multiple values that can be decomposed
      // the standard technique for un-applying a list don't work in this case and hence, the UNAPPLY sequence is used
  }

  abstract class advList[+A] {
    def head: A = ???
    def tail: advList[A] = ???
  }

  case object advEmpty extends advList[Nothing]
  case class advCons[+A](override val head: A, override val tail: advList[A]) extends advList[A]

  object advList {
    def unapplySeq[A](list: advList[A]): Option[Seq[A]] =
      if(list == advEmpty) Some(Seq.empty)
      else unapplySeq(list.tail).map(list.head +: _)
  }

  val myList: advList[Int] = advCons(1, advCons(2, advCons(3, advEmpty)))

  val decomposed = myList match {
    case advList(1, 2, _*) => "starting with 1, 2"
    case _ => "something else"
  }

  println(decomposed)
  // WORKING:
  // similar to unapply(), this pattern of advList needs to have either an unapply() or unapplySeq()
  // but since we wrote (1, 2, _*), the compiler expects an unapplySeq()
  // the compiler will look into the advList object and looks for a method unapplySeq()
  // the unapplySeq takes an arg of type advList[A] (basically a list which you later want to decompose) and returns an Option[] with a Seq[]

  // custom return types for unapply
  // it is not necessary to have an Option as a return type
  // the data structure used needs to have only two defined methods:
  // 1. isEmpty() which returns a boolean
  // 2. get() which returns something

  abstract class Wrapper[T] {
    def isEmpty: Boolean
    def get: T
  }

  object PersonWrapper {
    def unapply(person: Person): Wrapper[String] = new Wrapper[String] {
      override def isEmpty: Boolean = false
      override def get: String = person.name
    }
  }

  println(bob match {
    case PersonWrapper(n) => s"This person's name is $n"
    case _ => "Alien"
  })
}
