package exercises_adv

import lectures.part8_implicits.TypeClasses.User

object EqualityPlayground extends App {
  trait Equality[T] {
    def apply(a: T, b: T): Boolean
  }

  object Equality {
    def apply[T](a: T, b: T)(implicit equality: Equality[T]): Boolean = equality.equals(a, b)
  }

  implicit object NameEquality extends Equality[User] {
    override def apply(a: User, b: User): Boolean = a.name == b.name
  }

  object NameEmailEquality extends Equality[User] {
    override def apply(a: User, b: User): Boolean = a.name == b.name && a.email == b.email
  }

  val john = User("John", 32, "john@zd.com")
  val anotherJohn = User("John", 45, "john@gmail.com")
  println(Equality.equals(john, anotherJohn))

  /*
   * Exercise
   * - improve Equal TC with an implicit conversion class
   * - ===(anotherValue: T) this will invoke the Equal TC to the value this implicit conversion class will contain and the other value that is taken by the parameter
   * - !==(anotherValue: T) opposite to the above */

  implicit class EqualityEnrichment[T](value: T) {
    def ===(anotherValue: T)(implicit equalizer: Equality[T]): Boolean = equalizer.apply(anotherValue, value)
    def !==(anotherValue: T)(implicit equalizer: Equality[T]): Boolean = equalizer.apply(anotherValue, value)
  }

  println(john === anotherJohn)
  /*
   * john.===(anotherJohn)
   * new TypeSafeEqual[User](john).===(anotherJohn)
   * new TypeSafeEqual[User](john).===(anotherJohn)(NameEquality)
   * */
  println(john.===(anotherJohn)(NameEquality))
  println(john.===(anotherJohn)(NameEmailEquality))
  println(john.!==(anotherJohn)(NameEquality))
  println(john.!==(anotherJohn)(NameEmailEquality))

  // it is type safe
//  println(john === 43) compiler refuses to compile unless the two operands are of equal types
}
