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
   * 2. only ONE implementation out of a lot of possible implementations (e.g. if a user it logged in etc.) */

}
