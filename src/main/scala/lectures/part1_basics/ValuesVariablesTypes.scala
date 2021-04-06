package lectures.part1_basics

object ValuesVariablesTypes extends App {

  // VALUES

  val x1 = 42
  val str1: String = "hello" // semi-colons are allowed but not necessary. It is necessary only if you write multiple expressions in one line
  val str2: String = "goodbye"

  val bool: Boolean = false;
  val ch: Char = 'a'
  val x2: Int = x1
  val sht: Short = 4613
  val lng: Long = 1230871380731508L // the 'L' at the end is to mark this number as long
  val flt: Float = 2.0f // 'f' marks this number as a Float. If you don't add the 'f', the compiler will interpret it as a Double
  val dbl: Double = 3.14

  // VARIABLES

  var aVar: Int = 4

  println(x1 + ":" + aVar)

  aVar = 5 // side-effects

  println(x1 + ":" + aVar)
}
