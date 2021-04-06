package lectures.part1_basics

object CBNvsCBV extends App {
  def calledByValue(x: Long): Unit = {
    println("By value: " + x)
    println("By value: " + x)
  }

  def calledByName(x: => Long): Unit = { // The => "Arrow" tells the compiler that parameter will be called by name
    println("By name: " + x)
    println("By name: " + x)
  }

  calledByValue(System.nanoTime())
  calledByName(System.nanoTime())

  def infinite(): Int = 1 + infinite()
  def printFirst(x: Int, y: => Int): Unit = println(x)

//  printFirst(infinite(), 34) // Evaluates infinite() first and crashes due to a StackOverflow Error!
  printFirst(34, infinite()) // By swapping the arguments, the function call to printFirst() runs without crashing
  // This happened because the second argument is "CallByName" and delays the evaluation of the expression passed until its used
  // and since the param "y" is never used inside the function definition of printFirst(), the program never calls infinite() and therefore never crashes
}
