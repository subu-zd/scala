package lectures.part1_basics

import scala.annotation.tailrec

object Functions extends App {

  def func(a: String, b: Int): String = {
    a + " " + b
  }

  println(func("hello", 3))

  def noParamFunc(): Int = 42

  println(noParamFunc())

  def repeatedFunction(str: String, n: Int): String = {
    if(n == 1) str
    else str + repeatedFunction(str, n - 1)
  }
  // In Scala or a functional programming language, one would use recursive functions instead of loops
  println(repeatedFunction("Hello", 3))

  // Just like vars and vals, the compiler can infer the return type of functions if not explicitly stated
  // However, the compiler can not infer the return type of recursive function
  // We can define a function whose return type is 'unit', i.e. a function with only side-effects

  def sideEffectsFunc(str2: String): Unit = {
    println(str2)
  } // functions with side-effects are generally used when we are creating functions which have nothing to do with computation itself.

  // functions with code blocks allow us to define auxiliary functions inside the code block

  def bigFunc(n: Int): Int = {
    def smallFunc(a: Int, b: Int): Int = a + b

    smallFunc(n, n - 1);
  }

  println(bigFunc(7))

  /* EXERCISE
  * 1. A greeting function (name, age) => "Hi, my name is $name and I am $age years old"
  * 2. Factorial function
  * 3. Fibonacci function
  * 4. Tests if a number is prime
  * */

  // Q1.
  def q1(nameString: String, ageString: Int): String = {
    "Hi! My name is " + nameString + " and I am " + ageString + " years old."
  }

  println(q1("Subu", 22))

  // Q2.
  def q2(n: Int): Int = {
    if(n <= 0) 1
    else n * q2(n - 1)
  }

  // Q3.
  println(q2(6))

  def q3(n: Int): Int = {
    if(n == 2 || n == 1) 1
    else q3(n - 1) + q3(n - 2)
  }

  println(q3(8))

  // Q4.
  def outerQ4(x: Int): String = {
    @tailrec
    def q4(i: Int): String = {
      if(i <= 1) "Prime"
      else if(x % i == 0) "Not Prime"
      else q4(i - 1)
    }

    q4(x / 2)
  }


  println(outerQ4(2003))
  println(outerQ4(37 * 17))

}
