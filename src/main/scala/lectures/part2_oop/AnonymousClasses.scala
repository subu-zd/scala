package lectures.part2_oop

object AnonymousClasses extends App {

  abstract class Animal {
    def eat(): Unit
  }

  val funAnimal: Animal = new Animal {
    override def eat(): Unit = println("hahaha")
  }

  /* above code is equivalent to:
  *
  * class AnonymousClasses$$anon$1 extends Animal {
  *   override def eat(): unit = println("hahaha")
  * }
  *
  * val funAnimal: Animal = new AnonymousClasses$$anon$1
  * */

  // by the look of it, it looks like we instantiated an abstract class but in actuality, we instantiated an actual class
  println(funAnimal.getClass)

  // the compiler took this part:
  /*
    Animal {
      override def eat(): Unit = println("hahaha")
    }
  * */
  // and created a class with the name "lectures.part2_oop.AnonymousClasses$$anon$1",
  // instantiated that later and assigned that to funAnimal

  // this is called an Anonymous class
  // so when we write "new Animal" with an on-the-spot implementation, the compiler actually creates an anonymous class
  // (in this case a class named "lectures.part2_oop.AnonymousClasses$$anon$1") that extends Animal
  // and instantiates that class

  class Person(name: String) {
    def sayHi(): Unit = println(s"Hi, name is $name, how can i help?")
  }

  val jim = new Person("Jim") {
    override def sayHi(): Unit = println(s"Hi, how can I be of service?")
  } // anonymous classes work for both abstract and non abstract data types
  /*
    val jim = new Person {
      override def sayHi(): Unit = println(s"Hi, how can I be of service?")
    }

    the above code will not run because just as it was illegal to extend the class person w/o supplying the proper parameters,
    it as illegal here to instantiate an anonymous class here because the derivation or the extension of the class already happens under the hood which
    does require the parameters!

    so it is imperative to pass in the proper parameters
  */

  // it is also important to ensure that we must provide implementations for all the abstract fields or methods in teh abstract types or traits that
  // we instantiate anonymously, else the compiler won't be able to create a non abstract class in the background

}
