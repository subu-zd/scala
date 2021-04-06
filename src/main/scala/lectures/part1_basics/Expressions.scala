package lectures.part1_basics

object Expressions extends App {
  var x = 1 + 2 // expressions. they are evaluated to a value and type (which in this case is Int)

  println(x)

  println(2 + 3 * 4) // they preserve ordering of multiplication
  // +, -, *, /, &, |, ^, <<, >>, >>> (right shift with zero extensions)

  println(1 == x)
  // ==, !=, >, >=, <, <=

  println(!(1 == x))
  // ! && ||

  var y = 2
  y += 3 // also works with -=, *=, /= ... side effects

  println(y)

  // Instructions (to do) vs Expressions (computing a value)
  // Instructions are executed (like Java), expressions are evaluated(Scala)
  // Scala forces everything to be an expression
  // IF-ELSE in Scala is an expression as it always computes a value

  val aCondition = true
  val aConditionValue = if(aCondition) 5 else 3

  println(aConditionValue)

  // There are LOOPs in Scala but it is generally discouraged because they are reminiscent of imperative programming like Java.
  // They often don't return anything meaningful and only cause side effects

  // Imperative programming is a paradigm of computer programming where the program describes steps that change the state of the computer.
  // Unlike declarative programming, which describes "what" a program should accomplish, imperative programming explicitly tells the computer "how" to accomplish it

//  var i = 0
//  while(i < 10) {
//    println(i)
//    i += 1
//  }
  // NEVER WRITE THIS AGAIN. Avoid any type of loops!


  val aWeirdValue: Unit = (x = 3) // the data type of aWeirdValue is Unit. Unit == Void. It doesn't return anything meaningful
  println(aWeirdValue) // return '()' i.e. empty brackets. This is the only value the 'unit' type can hold
  // side-effects in Scala are actually expressions that return 'unit'
  // while loops are also expressions that return 'unit'
  // println() is also a side-effect

  // CODE BLOCKS

  val codeBlock = {

    // values declared inside the code block are local to the code block and cannot be accessed outside
    val y = 2
    val z = y + 1
    if(z > 2) "hello" else "goodbye"
  }

  println(codeBlock)
  // a code block is also an expression
  // the value of the block is the value of the last expression inside the block


}
