package lectures.part2_oop

object Generics extends App {

  class MyList[+A] { // <- a parameterised class with type A
    // the type A in the square brackets denotes a generic type
    // once defined, the type A can be used inside the class definition
    def add[B >: A](ele: B): MyList[B]  = throw new NotImplementedError()
    // If to a list of A , we put in a B, which is a supertype of A, then this list will turn into a list of B and not a list of A
    /*
    * A = Cat
    * B = Dog = Animal
    *
    * if we add a Dog i.e. an Animal to a list of Cats, then the list of Cats will become a list of Animals
    * */
  }

  class MyMap[Key, Value] { // you can have multiple type parameters

  }

  // Generics also work with traits and hence traits can also be type parameterised

  val intList = new MyList[Int] // the type in between the square brackets will then replace the generic type for this instance of list of integers
  val stringList = new MyList[String]

  // generic method

  object MyList { // objects can not be type parameterised
    def empty[A]: MyList[A] = throw new NotImplementedError() // method signature type parameterised with a generic type parameter
  }

//  val emptyIntList = MyList.empty[Int]

  // variance problem

  class Animal
  class Cat extends Animal
  class Dog extends Animal

  /* If Cat extends Animal, does a list of Cats extends a list of Animals
  *  3 Options :->
  *
  * 1. Yes, list of Cats extends a list of Animals. this behaviour is called COVARIANCE */

  // covariant list definition
  class CovList[+A]
  val animal: Animal = new Cat
  val animalList: CovList[Animal] = new CovList[Cat]

  // question! can you add an animal to it?
  // e.g. animalList.add(new Dog) ?
  // adding a dog to a list of cats will actually turn the list of Cats into a generic list of Animals

  // 2. No! List of Cats and List of Animals are two separate things. INVARIANCE

  // invariant list definition
  class InvariantList[A]
  // invariant classes are each in its own world and you cannot substitute one for another

//  val invAnimalList: InvariantList[Animal] = new InvariantList[Cat] - this line of code will not compile
  //  val invAnimalList: InvariantList[Animal] = new InvariantList[Animal] - this line of code will compile

  // 3. CONTRAVARIANCE
  // exact opposite relationship of covariant lists! (counter-intuitive)

  class ContravariantList[-A]
  val contraList: ContravariantList[Cat] = new ContravariantList[Animal]

  // BOUNDED Types
  // allow you to use generic classes only for certain types that are either a subclass of a different type or a superclass of a different type

  class Cage[A <: Animal](animal: A) // class Cage only accepts type parameters A that are subtypes of class Animal
  // this example specifically ia an upper bounded type

  val cage = new Cage(new Dog) // this expression evaluates to something which is an animal, hence acceptable!

//  class Car
//  val cage2 = new Cage(new Car)
  // the above code will give an error as class Car is NOT a subtype of class Animal

  // lower bounded type
  class AnotherCage[A >: Dog](animal: A)

  val newCage = new Cage(new Animal) // only accepts super type of Dog, hence Animal is also accepted

}
