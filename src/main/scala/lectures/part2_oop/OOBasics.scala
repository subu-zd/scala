package lectures.part2_oop

object OOBasics extends App{
  val person = new Person("John", 26)

  // you can't directly access person.age or person.names as in Scala, class parameters are not FIELDS (these are two different things)
  // to convert a class parameter to a class field, you have add the "val" / "var" keyword before the parameter declaration in the constructor
  // However, this conversion is not necessary
  println(person.x)
  person.greet("Daniel")
  person.greet()

  val author = new Writer("Mark", "Dickens", 1835)
  val imposter = new Writer("Mark", "Dickens", 1835)
  val novel = new Novel("Tom Sawyer", 1876, author)

  println(novel.authorAge())
  println(novel.isWrittenBy(author))
  println(novel.isWrittenBy(imposter))

  val counter = new Counter()
  counter.inc().print()
  counter.inc().inc().inc().print()
  counter.inc(10).print()
}

class Person(name: String, val age: Int) { // constructor
  // body: val/var definitions, method definitions, expressions, complex definitions such as other classes and packages

  val x: Int = 2 // val/var definitions can be accessed outside the class as fields
  println(1 + 3)

  def greet(name: String): Unit = println(s"${this.name} says: Hi, $name")
  // this.name refers to the instantiated class parameter irrespective of that fact if its a class field or not

  // method overloading
  def greet(): Unit = println(s"Hi, I am $name")
  // by default, if the method has no overlapping parameters as the class, by default the parameter accessed points to this.name (in this case "John")

  // Scala supports method overloading as long as you have not defined two methods with the same parameter list but returning different values
  // Scala also supports constructor overloading :->

  def this(name: String) = this(name, 0)
  // this auxiliary constructor accepts only the "name" parameter and calls the primary constructor with the age value as "0"
  // Limitation: Calling another primary or secondary constructor is the the only permitted implementation of an auxiliary constructor
  // and in such cases, it much easier to supply a default parameter to the primary constructor instead of declaring an auxiliary constructor

}

/*
* EXERCISE 1
* novel and writer
*
* writer: first name, surname, year of birth
*   method: returns concat of first name and surname
*
* novel: name , year of release, author(instance of type writer)
*   method: author age (when the novel was released)
*           isWrittenBy (returns author name)
*           copy (receives a new year of release and returns a new instance of novel with a new year of release) */

class Writer(firstName: String, lastName: String, val YOB: Int) {
  def fullName():String = firstName + " " + lastName
}

class Novel(name: String, YOR: Int, author: Writer) {
  def authorAge(): Int = YOR - author.YOB
  def isWrittenBy(author: Writer): Boolean = author == this.author
  def copy(newYOR: Int): Novel = new Novel(name, newYOR, author)

}

/*
* EXERCISE 2
* Counter class
*   receives an int
*   method: returns current count
*           increment/decrement current count (by 1) returns new Counter
*           overload inc/dec to receive an amount by which you inc/dec the counter. also returns new Counter */

//class Counter(ctr: Int) {
//  def count(): Int = ctr // functions as a getter. Instead of declaring a method, we can do the following
//}

// alternative
// this has the same effect as defining a method that gets that field

class Counter(val count: Int = 0) {
//  def inc(): Counter = new Counter(count + 1)

  // the fact that we are defining a new counter instead of incrementing the current counter is called immutability
  // this is an imperative concept of functional programming that basically states that instances are fixed
  // and if an instance value needs to be modified, we have to create a new instance with the updated value!

//  def dec(): Counter = new Counter(count - 1)
//
//  def inc(i: Int): Counter = new Counter(count + i)
//  def dec(i: Int): Counter = new Counter(count - i)

  // in case you want to log these incs/decs by using side-effects such as a println("incrementing"),
  // for the overloaded functions where you are inc/dec n times, you can do the following :->
  def inc(): Counter = {
    println("incrementing")
    new Counter(count + 1)
  }

  def dec(): Counter = {
    println("decrementing")
    new Counter(count - 1)
  }

  def inc(n: Int): Counter = {
    if(n <= 0) this
    else inc().inc(n - 1)
  }

  def dec(n: Int): Counter = {
    if(n <= 0) this
    else dec().dec(n - 1)
  }

  def print(): Unit = println(count)
}