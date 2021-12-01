package lectures.part2_oop

object AbstractDataTypes extends App {
  // abstract classes
  // classes which contain unimplemented or abstract fields or methods
  // this is usually when you want subclasses to provide specific implementation of parent class field and methods
  // abstract classes can not be instantiated

  abstract class Animal {
    val creatureType: String = "Wild"
    def eat(): Unit
  }

  class Dog extends Animal {
    override val creatureType: String = "K9"

    override def eat(): Unit = println("nomnom")
    // the override keyword prefix is not necessary since the parent abstract class doesn't have an implementation of any method/field
  }
  // abstract classes have both abstract and non-abstract fields and methods but so can Traits
  // Traits
  trait Carnivore {
    def eat(animal: Animal): Unit

    val preferredMeal: String = "Meat"
    // traits, by default, like abstract classes have abstract fields and methods
    // but unlike abstract classes, traits can be inherited along with classes
  }

  trait ColdBlooded // you can mix in as many traits as you want

  class Crocodile extends Animal with Carnivore with ColdBlooded {

    override val creatureType: String = "croc"

    override def eat(): Unit = println("crunch")

    override def eat(animal: Animal): Unit = println(s"I am a croc and I am eating a ${animal.creatureType}")
  }

  val dog = new Dog
  val croc = new Crocodile
  croc.eat(dog)

  /*
  * Traits vs Abstract Classes
  *
  * 1. unlike abstract classes, traits do not have constructor parameters
  * 2. you can only extend ONE class but you extend MULTIPLE traits (this is how you implement multiple inheritance in Scala)
  * 3. we choose Traits over an Abstract class as it describes BEHAVIOUR while a class describes a type of THING
  *
  */
}
