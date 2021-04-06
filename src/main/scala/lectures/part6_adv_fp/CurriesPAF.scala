package lectures.part6_adv_fp

object CurriesPAF extends App {
  // curried functions
  val superAdder: Int => Int => Int =
    x => y => x + y

  val add3 = superAdder(3)
  println(add3(5))
  println(superAdder(3)(5)) // curried function, as it can receive multiple parameter lists

  def curriedAdder(x: Int)(y: Int): Int = x + y // curried method

  val add4: Int => Int = curriedAdder(4)
  // If we don't explicitly define the return type for add4,
  // the compiler throws and exception stating that we are attempting to call a method with fewer parameter lists than expected
  // the compiler can't decide if its defining a function or receiving the result of curriedAdder
  // by specifying the parameter, the compiler realises that it wants a remainder function after a curriedAdder with fewer parameter list

  // under the hood, this process is called LIFTING
  // now because curriedAdder is a method, when called we need to supply the entire parameter list
  // add4 converts the method into a function of type Int => Int
  // we want this because in FP, unlike function values, we can't use methods in HOFs unless they're transformed into function values

  // this is a limitation of JVM. Methods are part of instances of classes (in this case part of the singleton object CurriesPAF)
  // methods are not instances of FunctionX
  // so transforming the method to function is called LIFTING

  // aka ETA-Expansion (Technical term)
  // it's technique for wrapping functions into this extra layer while preserving identical functionality
  // this is performed by the compiler to create functions out of methods (function != method)

  def inc(x: Int) = x + 1
  List(1, 2, 3).map(inc)
  // in this e.g. the compiler does ETA-Expansion under the hood and
  // converts inc method into a function and then it uses that function value on maps
  // essentially rewrites it as this lambda List(1, 2, 3).map(x => inc(x))

  // We can force the compiler to do ETA-Expansion when we want to use
  // Partial Function Applications
  val add5 = curriedAdder(5) _
  // the underscore "_" forces the compiler to convert it into an ETA-Expansion while taking 5 as the first parameter list

  /*
  * EXERCISE
  * */

  val simpleAddFunction = (x: Int, y: Int) => x + y
  def simpleAddMethod(x: Int, y: Int) = x + y
  def curriedAddMethod(x: Int)(y: Int) = x + y

  // add7: Int => Int = y = 7 + y
  // implement as many different implementations of add7 using the above

  val add7_1 = (x: Int) => simpleAddFunction(7, x)

  val add7_2 = simpleAddFunction.curried(7)
  val add7_6 = simpleAddFunction(7, _: Int)

  val add7_3 = curriedAddMethod(7) _ // PAF
  val add7_4 = curriedAddMethod(7)(_) // alternative syntax

  val add7_5 = simpleAddMethod(7, _: Int) // alternative syntax for turning methods into function values
              // y => simpleAddMethod(7, y)

  // underscores are powerful
  def concatenate(a: String, b: String, c: String) = a + b + c
  val insertName = concatenate("Hello, I'm ", _: String, ", how are you?")
  // performs ETA exp and re-writes it as x: String => concatenate("Hello, I'm ", x, ", how are you?")
  println(insertName("Subu"))

  val fillInTheBlanks = concatenate("Hello ", _: String, _: String)
  // (x, y) => concatenate("Hello", x, y)
  println(fillInTheBlanks("Subu, ", "Scala rocks!"))

  // EXERCISE
  // 1. Process a list of numbers and return their string representations with different formats
  //    Use %4.2f, %8.6f and %14.12f with a curried formatter function

  println("%8.6f".format(Math.PI))

  def curriedFormatter(s: String)(n: Double): String = s.format(n)
  val nos = List(Math.PI, Math.E, 1, 9.8, 1.3e-12)

  val simpleFormat = curriedFormatter("%4.2f") _ // lift
  val seriousFormat = curriedFormatter("%8.6f") _
  val preciseFormat = curriedFormatter("%14.12f") _

  println(nos.map(simpleFormat))
  println(nos.map(seriousFormat))
  println(nos.map(curriedFormatter("%14.12f")))
  // in this case, since we're passing a method with fewer params, the compiler does the ETA expansion automatically

  // 2. difference  between
  //    - functions vs methods
  //    - parameters: by-name vs 0-lambda

  def byName(n: => Int) = n + 1
  def byFunction(f: () => Int):Int = f() + 1

  def method: Int = 42
  def parenMethod(): Int = 42

  /*
  * calling byName and byFunction
  * - int
  * - method
  * - parenMethod
  * - lambda
  * - PAF
  * */

  byName(23)
  byName(method)
  byName(parenMethod())
  byName(parenMethod) // ok but beware ==> byName(parenMethod())

  // it's a common mistake that people confuse the byName() parameter with a HOF
  // and they expect byName with a function to actually use the function inside

  //  byName(() => 42)
  // this throws an error because byName argument of value type is not the same as a function parameter
  byName((() => 42)()) // ok

//  byName(parenMethod _)
  // a function value is not a substitute of byName parameter

//  byFunction(45) // not OK
//  byFunction(method) // not ok
  // in the above case, you would expect method to be converted into a function value
  // but here method is evaluated to its value i.e. 42
  // and the compiler doesn't do ETA-expansion

  byFunction(parenMethod _)
  byFunction(() => 46)
}
