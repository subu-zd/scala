package lectures.part1_basics

import scala.annotation.tailrec

object Recursion extends App {

  def fact(n: Int): Int = {
    if(n <= 1) {
      println("factorial of 1 is " + 1)
      val res = 1
      println("Computed factorial of " + n)
      res
    } else {
      println("Computing factorial of " + n + " by first computing factorial of " + (n - 1))
      val res = n * fact(n - 1)
      println("Computed factorial of " + n)
      res
    }
  }

  def outerFact(n: Int): Int = {
    @tailrec
    def helper(x: Int, accumulator: Int): Int = {
      if(x <= 1) accumulator
      else helper(x - 1, x * accumulator)
    }

    helper(n, 1)
  }

  /*
  outerFact(5) = helper(5, 1)
    helper(4, 5 * 1)
    helper(3, 4 * 5 * 1)
    helper(2, 3 * 4 * 5 * 1)
    helper(1, 2 * 3 * 4 * 5 * 1)
  */

  println(outerFact(5000))

  /*
  * 1. Concatenate a string n times
  * 2. isPrime
  * 3. Fibonacci
  */

  // Q1.
  def concatenate(str: String, n: Int): String = {
    @tailrec
    def helper(s: String, i: Int, acc: String): String = {
      if(i <= 0) acc
      else helper(s, i - 1, s + acc);
    }

    helper(str, n, "")
  }

  println(concatenate("Hello", 6))


  // Q2.
  def isPrime(n: Int):String = {
    @tailrec
    def helper(i: Int, div: Int): String = {
      if (div == 1) "Prime"
      else if (i % div == 0) "Not Prime"
      else helper(i, div - 1)
    }

    helper(n, n / 2)
  }

  println(isPrime(10))
  
  // Q3
  def fibo(n: Int): Int = {
    @tailrec
    def helper(n: Int, a1: Int, a2: Int): Int = {
      if (n <= 1) a2
      else if (n == 2) a1 + a2
      else helper(n - 1, a2, a1 + a2)
    }

    helper(n, 1, 0);
  }

  println(fibo(4))
}
