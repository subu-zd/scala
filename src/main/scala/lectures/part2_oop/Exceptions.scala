package lectures.part2_oop

object Exceptions extends App {

//  val x: String = null
//  println(x.length)c // NullPointerException
  // when the compiler tries to access the null.length and it throws an exception,
  // there is nobody to catch it and hence, the JVM wil crash.

  // 1. throwing exceptions

//  val aWeirdVal: String = throw new NullPointerException
  // like everything in Scala, throwing an exception is an expression
  // although the above line is valid, the "throw new NullPointerException" returns nothing
  // so this doesn't actually hold a value but it can be assigned to things
  // and since "Nothing" is a valid substitute for any other type, the LHS can be a "String" in this case (or any other type)

  // the "new" keyword is because exceptions are actually instances of classes
  // exceptions are throwable classes that extends the Throwable class
  // the type Exception and Error are the 2 major throwable subtypes
  // both will crash the JVM but at the semantic level,
  // Exceptions indicate that there is something wrong with the program e.g. ArithmeticException
  // Errors indicate that there is something wrong with the system e.g. StackOverflowError

  // so exceptions are basically instances of special classes that derive from Exception or Error.
  // one rarely needs in practice to derive a class from Error
  // one most usually need Exceptions

  // 2. catching exceptions

  def getInt(withExceptions: Boolean): Int = {
    if(withExceptions) throw new RuntimeException("No Int for you :)")
    else 42
  }

  // exceptions in Scala come from the java language

  val potentialFail = try {
    // code that might throw an exception
    getInt(true)
  } catch {
    case e: RuntimeException => println("caught a RunTimeException: " + e)
  } finally {
    // code that will get executed no matter what
    println("finally")
    // the "finally" block is optional and it doesn't influence the return type of this expression
    // it is recommended to use "finally" for only side effects e.g. logging something to a file when writing distributed applications
  }

  // try-catch-finally is an expression in Scala
  // if we assign try-catch-finally to val, then the type of potentialFail is AnyVal
  // this is because the "try" block tries to return an Int
  // but the value returned from the "catch" block is Unit
  // so when the compiler tries to unify Int and Unit, it gets AnyVal

  /*
    class MyException extends Exception
    val exception = new MyException

    throw exception
  */

  /*
  * since custom exceptions are classes, like any other class, they can have parameters, fields, methods etc.
  * However, the above are rarely needed in practice
  * Commonly, one needs a "name" and a "print stack trace" utility method
  */
}
