package lectures.part2_oop

object Inheritance extends App {
  // Scala offers single class inheritance i.e. you can only extend one class at a time
  // a scala subclass only inherits the non-private members of the superclass
  // private access modifier allows the var/val/method to be accessed in that class only
  // protected access modifier allows the var/val/method to be used in that class and the subclass

  class Animal {
    val creatureType: String = "Wild"
    def eat(): Unit = println("nom nom nom")
  }

  class Cat extends Animal {
    def crunch(): Unit = {
      eat()
      println("crunch crunch")
    }
  }

  val cat = new Cat
  cat.crunch()

  // constructors
  class Person(name: String, age: Int)

//  class Adult(name: String, age: Int, idCard: String) extends Person

  // the above line doesn't compile as the in Scala defining a class with the class signature also defines its constructor
  // in this case, when you instantiate the derived class "Adult", the JVM needs to call the constructor from the Parent Class first
  // the compiler forces you to guarantee that there is a super constructed code when using a derived class

  class Adult(name: String, age: Int, idCard: String) extends Person(name, age)

  // ANOTHER ALTERNATIVE (using an auxiliary constructor):

  /*
  * class Person(name: String, age: Int) {
  *   def this(name: String) = this(name, 0)
  * }
  *
  * class Adult(name: String, age: Int, idCard: String) extends Person(name)
  *
  * In this case, the compiler found the auxiliary constructor with just the name for the superclass
  * */

  // OVERRIDING

  // overriding works for methods, vals and vars
  // all instances of the derived classes use the overridden things whenever possible

  class Dog(override val creatureType: String) extends Animal {
//    override val creatureType: String = "domestic"

    // fields as opposed to methods have the special property that they can be overridden directly in the constructor

    // super
    // it is used to reference a method or field from a parent class

    override def eat(): Unit = {
      super.eat()
      println("crunch nom")
    }
  }

  val dog = new Dog("K9")
  dog.eat()
  println(dog.creatureType)

  // Type-substitution (broad sense: Polymorphism)
  val unknown: Animal = new Dog("K9")
  unknown.eat() // this line prints "crunch nom" and NOT "nom nom nom" as the method call will always go to the most overridden version whenever possible

  // preventing overrides
  // 1. using the keyword "final" on a member
  //    using a final keyword before methods/fields prevents derived classes from overriding that method/field
  //
  // 2. using the keyword "final" on a class
  //    this prevents the entire class from being extended
  //
  // 3. Sealing the class (using the keyword "sealed" in the class definition)
  //    it is a software restriction where you can extend classes in this file but prevents extension in other files
  //    the sealed keyword is used when you want to be exhaustive in your type hierarchy
  // E.g. if the only two possible animals in the world are Cat and Dog (classes in this file),
  //      then you would seal the class Animal and extend Cat and Dog in this file and prevent Animal from being extended in other files
}
