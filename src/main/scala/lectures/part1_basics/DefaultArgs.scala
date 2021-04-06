package lectures.part1_basics

import scala.annotation.tailrec

object DefaultArgs extends App {

  @tailrec
  def trFact(x: Int, acc: Int): Int = {
    if(x <= 1) acc
    else trFact(x - 1, x * acc)
  }

  val factAns = trFact(10, 1) // In the case of Factorial, the accumulator argument is always given a value of 1 in the function's initial call
  println(factAns)
  /*
  * This causes 1 of 2 things:
  * EITHER this acc pollutes the function signature because we're only interested in the "n" parameter
  * OR we need to wrap this function as an auxiliary function inside a bigger function
  */

  // Scala allows us to pass default values for some parameters that we don't really want to appear in every single call
  // This is done by specifying a default value at the parameter level

  @tailrec
  def defFact(x: Int, acc: Int = 1): Int = { // The "= 1" implies that if a value for the acc is not explicitly passed then the default value is used
    if(x <= 1) acc
    else defFact(x - 1, x * acc)
  }

  println(defFact(10))

}
