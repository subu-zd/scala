package lectures.part8_implicits

object TypeClasses extends App {
  // A Type Class is a trait that takes a type and describes what operations can be applied to that type

  // e.g. backend of a small social network with server-side rendering
  trait HTMLWritable {
    def toHTML: String
  }

  case class User(name: String, age: Int, email: String) extends HTMLWritable {
    override def toHTML: String = s"<div>$name ($age yo) <a href=$email/> </div>"
  }

  User("John", 32, "john@rockthejvm.com").toHTML
  /* DISADVANTAGES
   * 1. this will only work for the types we write
   * 2. only ONE implementation out of a lot of possible implementations (e.g. if a user is logged in or not etc.) */

  // option 2 - pattern matching
  object HTMLSerializerPM {
    def serializeToHtml(value: Any) = value match {
      case User(n, a, e) =>
//      case java.util.Date =>
      case _ =>
    }
  }

  /* Disadvantages
   * 1 - Lost type safety
   * 2 - need to modify the code every time
   * 3 - still ONE implementation
   * */

  // better design
  trait HTMLSerializer[T] {
    def serialize(value: T): String
  }

  implicit object UserSerializer extends HTMLSerializer[User] {
    def serialize(user: User): String = s"<div>${user.name} (${user.age} yo) <a href=${user.email}/> </div>"
  }

  val john = User("John", 32, "john@zd.com")
  println(UserSerializer.serialize(john))

  // 1. We can define serializer for other types (even types that we've NOT written)

  import java.util.Date
  object DateSerializer extends HTMLSerializer[Date] {
    override def serialize(date: Date): String = s"<div>${date.toString}</div>"
  }

  // 2 - we can define multiple serializers for a certain type
  object PartialUserSerializer extends HTMLSerializer[User] {
    def serialize(user: User): String = s"<div>${user.name}</div>"
  }
  // this also guarantees type safety because the HTML serializer can take any type parameter
  // The HTMLSerializer[T] trait defined above is called a TYPE CLASS
  // A type class specifies a set of operations, in this case serialize, that can be applied to a given type
  // so in this case, anyone who extends HTMLSerializer needs to provide this functionality
  // All the implementers of a type class are called type class instances

  // In this case, even though the type class instances are types of themselves, such as UserSerializer,
  // they're called instances because it doesn't make sense to instantiate them multiple times
  // That's why we often use Singleton objects for them

  /* TYPE CLASSES as an abstract concept
   *
   * As a data type, a normal class describes a collection of methods and properties that something must have in order to belong to a specific type.
   * For e.g. if it is known for an object to be of type String, then it is known to support the ".length" operation
   * And the type checker of the compiler can use this info at compile time to find errors in the source code. This is a process called static type checking.
   *
   * A Type Class as opposed to a normal class, lifts this same concept to a higher level, applying it to TYPES
   * It describes a collection of properties or of methods a type must have in order to belong to that specific type class
   * For e.g. if it is known that a type belongs to an ordering type class, then it is known that instances of that type have the ability to compare values and tell if one is less than another.
   * */

  // type class template
  trait MyTypeClassTemplate[T] {
    def action(value: T): String
  }

  object MyTypeClassTemplate {
    def apply[T](implicit instance: MyTypeClassTemplate[T]) = instance
  }

  /* Equality
   * - it has a method equals which compares two values
   * - also implement two instances of this equal type class compare users by name and by both name and email
   * */

//  trait Equality[T] {
//    def apply(a: T, b: T): Boolean
//  }
//
//  object NameEquality extends Equality[User] {
//    override def apply(a: User, b: User): Boolean = a.name == b.name
//  }
//
//  object NameEmailEquality extends Equality[User] {
//    override def apply(a: User, b: User): Boolean = a.name == b.name && a.email == b.email
//  }

  // part 2
  object HTMLSerializer {
    def serialize[T](value: T)(implicit serializer: HTMLSerializer[T]): String = serializer.serialize(value)

    def apply[T](implicit serializer: HTMLSerializer[T]) = serializer
  }

  implicit object IntSerializer extends HTMLSerializer[Int] {
    override def serialize(value: Int): String = s"<div>$value</div>"
  }

  println(HTMLSerializer.serialize(42))
  // by making the IntSerializer object implicit as well, the compiler will figure out that the first arg (i.e. 42) is an Int
  // and We need an implicit serializer as an HTMLSerializer of type Int
  // the compiler finds the implicit object IntSerializer and calls that
  println(HTMLSerializer.serialize(john))
  println(HTMLSerializer[User].serialize(john)) // by this, we access to the entire type class interface

  /* Exercise
   * implement the TC pattern for the equality type class */

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

  val anotherJohn = User("John", 45, "john@gmail.com")
  println(Equality.equals(john, anotherJohn))
  // AD-HOC polymorphism
  // We achieve polymorphism in the sense that if two distinct or potentially unrelated types have equalizers implemented,
  // then we can call this equal functionality on them regardless of their types

  // In this case, we have equals on Users for e.g. and if we have an implicit equalizer for another type,
  // we can still call equals on those types as well

  // This is polymorphism because depending on the actual type of the values being compared,
  // the compiler takes case to fetch the correct type class instances for our types

}
