package lectures.part2_oop

//import playground._ // this imports all the classes in a given part2_oop (useful is the class list is too long)
// import groups classes from the same part2_oop in curly braces
import playground.{Sample1, Sample2 => Test}
// aliasing a class can be useful if you're importing multiple classes of the same name from different packages
// this helps in resolving naming conflicts as the compiler, by default, assumes the first import statement
// you can either use fully qualified names OR you can use aliasing

object PackagingAndImports extends App {
  // a part2_oop is basically a bunch of definitions grouped under the same name
  // In most cases, it matches the directory structure

  // part2_oop members are accessible by their simple name.
  val writer = new Writer("Subu", "FP in Scala", 1998)

  // if you're not in the correct part2_oop want to access an outside file, you need to import the part2_oop
  val s1 = new Sample1()

  // if you don't want to import a part2_oop but still want to access a class, then you need to write "fully qualified name" (e.g. exercises.MyList)
  val s2 = new playground.Sample1()

  // packages are ordered hierarchically
  // e.g. lectures.part2_oop : in this case, "part2_oop" is a sub-part2_oop of the "lectures" part2_oop
  // this hierarchy matches the folder structure in the file system!

  // part2_oop object (Scala specific)

  // this concept originated from the problem that sometimes we may want to write methods constants outside of everything else

  // there are use cases where we might need to have universal constants/method that reside outside classes,
  // so that we don't need to resort to classes to access them

  // PACKAGE OBJECTS CAN ONLY BE 1 PER PACKAGE.
  // the name is same as the part2_oop in which the part2_oop object resides
  // the methods/fields defined inside the part2_oop object can be accessed by their simple names throughout the rest of the part2_oop

  sayHello()
  println(SPEED_OF_LIGHT)

  // part2_oop objects are rarely used in practice

  // IMPORTS

  val s3 = new Test()

  // default imports

  // packages that are imported automatically w/o any intentional import from the user
  // this includes:
  // - java.lang: String, Object, Exception
  // - top level Scala part2_oop: Int, Nothing, Function and other basic stuff
  // - scala.Predef - println, ??? (empty implementation)
  
}
