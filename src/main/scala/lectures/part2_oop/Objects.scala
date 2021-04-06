package lectures.part2_oop

object Objects extends App {
  // SCALA DOESN'T HAVE CLASS LEVEL FUNCTIONALITY (doesn't know the concept of "static")
  // Scala manifests this behaviour by the use of "Objects" (!= instances of class)

  object Person{
    val n_eyes = 2
    def canFly: Boolean = false
//    def from(mother: Person, father: Person): Person = new Person("Bobby") // FACTORY METHOD
    def apply(mother: Person, father: Person): Person = new Person("Bobby") // often these factory methods are called "apply" instead of some other name

  }
  // an object can have vals, vars and method definitions
  // objects can be defined in a similar way that classes can, with the exception that objects DO NOT RECEIVE PARAMETERS

  // In Scala, an object is used as a singleton instance
  // Explanation: When you define an object "Person", you're defining its type but you're also defining its only instance
  // so the object is its own type plus it is its only instance

  // the fact that Scala objects are singleton instances by definition is an advantage, as no extra code is needed

  // it is conventional to write the class and object using the same name
  // this is to separate instance-level functionality (class) from "static"/class level functionality (object)
  // this pattern of writing classes and objects with the same name in the same scope is called COMPANIONS

  // this enables us to write all the code in either a class or an object and any code in this application will be accessed from some kind of instance
  // i.e. either a regular instance (class) or a singleton instance (object)

  // Scala is more object oriented than most object oriented languages including Java while also being a functional programming language

  // -------
  // In practice, objects usually have factory methods (in this case methods to build Persons)

  class Person(val name: String) {

  }

  println(Person.n_eyes)
  println(Person.canFly)

  // E.g.

  // NOTE
  val mary = new Person("Mary")
  val john = new Person("John)")
  println(mary == john)// this returns "false' as they are two DIFFERENT instances of class person

  val p1 = Person // this is the instance of the "Person" type and this is the only instance the "Person" type can have
  val p2 = Person
  println(p1 == p2) // p1 and p2 point to the same instance which is the object called "Person"

  val bobby = Person(mary, john)

  // Scala Application
  // a Scala application is only a Scala object with a very important method called:
  // def main(args: Array[String]): Unit
  // this because Scala apps are turned into JVM apps whose entry point need to be "public static void main(String[] args)"
  // static ~ Scala object
  // void ~ Unit
  // the "extends App" already has a "main" method by default
}
