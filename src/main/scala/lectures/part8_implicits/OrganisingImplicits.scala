package lectures.part8_implicits

object OrganisingImplicits extends App {
  println(List(1, 4, 5, 3, 2).sorted)
  /*
   * When sorting collections in Scala, we use a trait "Ordering" which takes implicit parameters
   * in the above println statement the "sorted" method takes an implicit "Ordering" value
   *
   * Running the above println statement outputs a sorted list which means that there is already an implicit ordering value for int
   * Scala looks for this in the package scala.Predef
   * scala.Predef is one the packages that are automatically imported whenever you're writing scala code in the IDE
   * */

  implicit val reverseOrdering: Ordering[Int] = Ordering.fromLessThan(_ > _)
  println(List(1, 4, 5, 3, 2).sorted)

  /*
   * the above println outputs the list in reverse order
   * Explicitly adding an implicit Ordering value reverseOrdering will make this value
   * take precedence over the value defined in scala.Predef
   *
   * adding => implicit val normalOrdering: Ordering[Int] = Ordering.fromLessThan(_ < _)
   * will crash the program because the compiler won't understand which implicit to use
   * */

  /* Implicits: (used as implicit parameters)
   *   - val / var
   *   - objects
   *   - accessor methods i.e. defs with no parentheses
   *
   * All of the above can be defined only within a class, object or trait.
   * They can not be defined as top-level.
   * */

  // EXERCISE

  case class Person(name: String, age: Int)

  val personList = List(
    Person("Steve", 30),
    Person("Amy", 22),
    Person("Army", 23),
    Person("John", 66),
    Person("Johnny", 65)
  )

//  implicit val alphabeticalOrdering: Ordering[Person] = Ordering.fromLessThan((a, b) => a.name.compareTo(b.name) < 0)
  object NameOrdering {
    implicit val alphabeticalOrdering: Ordering[Person] = Ordering.fromLessThan(_.name < _.name)
  }

  object AgeOrdering {
    implicit val ageOrdering: Ordering[Person] = Ordering.fromLessThan(_.age < _.age)
  }

//  import NameOrdering._
  import AgeOrdering._
  println(personList.sorted)

  /*
   * Implicit Scope
   * - normal scope (a.k.a Local Scope). This is the scope with the highest priority. This is basically where we write the code.
   * Hence, any code written in the normal scope (for e.g. alphabeticalOrdering) is taken as highest priority
   *
   * - imported scope (e.g. from FUTURES when we imported ExecutionContext implicits global and it was passed to the Futures apply method)
   *
   * - companion objects of all types involved in the method signature
   *  - List
   *  - Ordering
   *  - all the types involved
   * for e.g. in the case of "sorted" => def sorted[B >: A](implicit ord: Ordering[B]): List[B] (in our case it will return a List[B])
   *
   * in our case the compiler will look for implicit orderings in List because that's the type for which the sorted method is defined
   * then the Ordering companion object
   * and then all the types involved i.e. A and any supertype
   *
   * e.g.
   * object SomeObj {
   *   implicit val alphabeticalOrdering: Ordering[Person] = Ordering.fromLessThan(_.name < _.name)
   * }
   * this won't compile as the implicit val is no longer within any of the acceptable implicit scopes
   *
   * BUT, if it is inside the companion of object of "Person"
   * object Person {
   *   implicit val alphabeticalOrdering: Ordering[Person] = Ordering.fromLessThan(_.name < _.name)
   * }
   *
   * it will compile at the "all types involved" level
   * */

  // for BEST PRACTICES surrounding IMPLICITS, refer NOTES

  /*
   * EXERCISE
   *
   * 3 orderings
   * - total price = most used (50%)
   * - unit count = 25%
   * - unit price = 25%
   * */
  case class Purchase(nUnits: Int, unitPrice: Int)

  object Purchase {
    implicit val totalPriceOrder: Ordering[Purchase] = Ordering.fromLessThan((a, b) => (a.unitPrice * a.nUnits) < (b.unitPrice * b.nUnits))
  }

  object UnitCountOrdering {
    implicit val unitCountOrder: Ordering[Purchase] = Ordering.fromLessThan(_.nUnits < _.nUnits)
  }

  object UnitPriceOrdering {
    implicit val unitPriceOrder: Ordering[Purchase] = Ordering.fromLessThan(_.unitPrice < _.unitPrice)
  }

  val purchaseList = List(
    Purchase(10, 20),
    Purchase(2, 90),
    Purchase(40, 3)
  )

  import UnitPriceOrdering._
  println(purchaseList.sorted)
}
