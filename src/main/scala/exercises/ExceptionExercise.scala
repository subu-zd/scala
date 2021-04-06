package exercises

/*
  * EXERCISES
  *
  * 1. Crash your program with an OutOfMemoryError
  * 2. Crash with SOError
  * 3. Design a class PocketCalculator
  *   - add(x, y) 2 numbers
  *   - multiply(x, y)
  *   - divide(x, y)
  *   - subtract(x, y)
  *
  * Throw
  * - OverflowException if add(x, y) exceeds Int.MAX / Int.MIN value
  * - UnderflowException if subtract(x, y) exceeds Int.MIN value
  * - MathCalculationException for division by 0
  *
  * */

object ExceptionExercise extends App {
//  val ar = Array.ofDim(Int.MaxValue) // 1. Out of Memory error

//  def func(n: Int): Int = 1 + func(n) // 2. StackOverFlow Error
//
//  val crash = func(4)

  class OverflowException extends RuntimeException
  class UnderflowException extends RuntimeException
  class MathCalculationException extends RuntimeException("Division by 0")

  object PocketCalculator {
    def add(x:Int, y: Int): Int = {
      val res = x + y

      if(x > 0 && y > 0 && res < 0) throw new OverflowException
      else if(x < 0 && y < 0 && res > 0) throw new UnderflowException
      else res
    }

    def subtract(x: Int, y: Int): Int = {
      val res = x + y

      if(x > 0 && y < 0 && res < 0) throw new OverflowException
      else if(x < 0 && y < 0 && res > 0) throw new UnderflowException
      else res
    }

    def multiply(x: Int, y: Int): Int = {
      val res = x * y

      if(x > 0 && y > 0 && res < 0) throw new OverflowException
      else if(x > 0 && y < 0 && res > 0) throw new UnderflowException
      else if(x < 0 && y > 0 && res > 0) throw new UnderflowException
      else if(x < 0 && y < 0 && res < 0) throw new OverflowException
      else res
    }

    def divide(x: Int, y: Int): Int = {
      if(y == 0) throw new MathCalculationException
      else x / y
    }
  }

  println(PocketCalculator.add(Int.MaxValue, -10))
  println(PocketCalculator.divide(2, 0))
}
