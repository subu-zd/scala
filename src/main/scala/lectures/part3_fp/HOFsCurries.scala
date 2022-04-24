package lectures.part3_fp

import scala.annotation.tailrec

object HOFsCurries extends App {
  val superFunction: (Int, (String, (Int => Boolean)) => Int) => (Int => Int) = null
  // the return type of the superFunction is a another function that takes an Int and returns and Int
  // the parameters for superFunction:
  // 1. Int
  // 2. another function that takes 2 parameters and returns and Int
  //    2.1 param 1 - String
  //    2.2 param 2 - another function that takes an Int as param and returns Boolean

  // functions that either take functions as params or return them as results are called HOFs

  // E.g. function that applies a function n times over a given value x
  // nTimes(f, n, x)
  // 3 params: function f, number n and value x

  // nTimes(f, n, x) = f(f(f(x))) = nTimes(f, n - 1, f(x)) ...

  @tailrec
  def nTimes(f: Int => Int, n: Int, x: Int): Int = {
    if (n <= 0) x
    else nTimes(f, n - 1, f(x))
  }

  val plusOne = (x: Int) => x + 1
  println(nTimes(plusOne, 20485708, 1))

  // nTB(f, n) = x => f(f(f(...(x)))
  // increment10 = nTB(plusOne, 10) = x => plusOne(plusOne...(x))
  // val y = increment10(1)

  def nTimesBetter(f: Int => Int, n: Int): (Int => Int) = {
    if (n <= 0) (x: Int) => x
    else (x: Int) => nTimesBetter(f, n - 1)(f(x))
  }

  val plus10 = nTimesBetter(plusOne, 10)
  println(plus10(1))

  // curried functions
  val superAdder: Int => (Int => Int) = (x: Int) => (y: Int) => x + y
  val add3 = superAdder(3)
  println(add3(10))

  // curried functions are very useful if you want to define helper functions that you want to use later on various values

  println(superAdder(3)(10))

  // functions with multiple parameter list

  def curriedFormatter(c: String)(x: Double): String = c.format(x)
  // Scala allows you to define a function with multiple parameter lists to act like curried functions

  val stdFormat: (Double => String) = curriedFormatter("%4.2f")
  val preciseFormat: (Double => String) = curriedFormatter("%10.8f")

  // NOTE (for functions with multiple param list):
  // if you want to define smaller functions later, you have to specify their type otherwise the code will not compile
  // Just an FYI: The proper way to handle this is by using Partial Function Applications (advanced subject)

  println(stdFormat(Math.PI))
  println(preciseFormat(Math.PI))

  /*
   * EXERCISES
   * 1. Expand MyList
   *   - forEach method
   *       - for each element in the list, it does a side effect:
   *       - it receive a function from (A => Unit) and it will apply this function
   *       - and this will apply this function to every element of my list
   *
   * e.g. [1, 2, 3].forEach(x => println(x)) this will print each element on a new line
   *
   *   - sort function ((A, A) => Int)
   *       - meaning it will be negative if left A is less than right A and positive otherwise
   *       - this will return another MyList ((A, A) => Int) => MyList
   *
   * e.g. [1, 2, 3].sort((x, y) => y - x) // saying that x is less than y if it's actually bigger than y, then this will sort the list in descending order i.e. [3, 2, 1]
   *
   *   - zipWith (list, (A, A) => B) => MyList[B]
   *
   * e.g. [1, 2, 3].zipWith([4, 5, 6], x * y) => [4, 10, 18]
   *
   *   - (curried) fold(start)(function) => a value
   *
   * e.g. [1, 2, 3].fold(0)(x + y) => 6
   *      the list will start to get folded in the sense that you will add the 1st element to 0 and then subsequent elements to the previous value (i.e. 2 to 1, then 3 to 3 (2 + 1) ...)
   *      and when you have no more elements in the list, then you return the final value
   *
   * 2. method to convert functions into curried and uncurried variants
   *    toCurry(f: (Int, Int) => Int) => (Int => Int => Int)
   *    fromCurry*f: (Int => Int => Int)) => (Int, Int) => Int
   *
   * 3. compose1(f, g) => x => f(g(x))
   *    compose2(f, g) => x => g(f(x))
   *
   * */

  // Q2

  def toCurry(f: (Int, Int) => Int): (Int => Int => Int) = { x => y =>
    f(x, y)
  }

  def fromCurry(f: (Int => Int => Int)): (Int, Int) => Int = { (x, y) =>
    f(x)(y)
  }

  def compose1[A, B, C](f: A => B, g: C => A): C => B =
    x => f(g(x))

  def compose2[A, B, C](f: A => B, g: B => C): A => C =
    x => g(f(x))

  def superAdder2: (Int => Int => Int) = toCurry(_ + _)
  def add = superAdder2(4)
  println(add(17))

  val simpleAdder = fromCurry(superAdder)
  println(simpleAdder(4, 17))

  val add2 = (x: Int) => x + 2
  val times3 = (x: Int) => x * 3

  val func1 = compose1(add2, times3)
  val func2 = compose2(add2, times3)

  println(func1(4))
  println(func2(4))
}
