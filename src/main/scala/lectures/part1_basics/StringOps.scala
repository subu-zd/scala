package lectures.part1_basics

object StringOps extends App {
  val str: String = "Hello! I am learning Scala"

  println(str.charAt(2))
  println(str.substring(7, 11))
  println(str.split(" ").toList)
  println(str.startsWith("Hello"))
  println(str.replace(" ", "-"))
  println(str.toLowerCase())
  println(str.length)

  // Scala's own utilities

  val numString = "45"
  val num = numString.toInt // Conversion

  print('a' +: numString :+ 'z') // Prepending (+:) and Appending (:+)

  println(str.reverse)
  println(str.take(8)) // Removes and returns the string of first n characters of the string

  // Scala-specific : String interpolators

  // S-interpolators
  val name: String = "David"
  val age: Int = 12
  val greeting: String = s"Hello! My name is $name and I am $age years old."
  val greeting2: String = s"Hello! My name is $name and I'll be turning ${age + 1}."

  println(greeting)
  println(greeting2)

  // F-interpolators
  val speed: Float = 1.2f
  val myth = f"$name%s can eat $speed%2.2f burgers per minute"
  // %s is string format
  // the %2.2f is float number format which means:
    // 2 characters total, minimum (adds preceding white spaces if $var is less than the minimum number)
    // and 2 decimals precision
  println(myth)

  //F-interpolated strings can also check for type-correctness in the values that they expand
  // E.g.

//  val x: Float = 1.1f
//  val str: String = f"$x%3d" // %3d expects Integer whereas x is Float. Hence, this will produce an error!

  // raw-interpolator

  // Works similar to S-interpolator but has the property that it can print characters literally

  println(raw"This is a \n newline") // because of the raw-interpolator, "\n" is not escaped and is printed literally.
  val escaped: String = "This is a \n newline"
  println(raw"$escaped") // injected variables however do get escaped.
}
