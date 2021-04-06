package lectures.part3_fp

object AnonymousFunctions extends App {
//  val doubler = new (Int => Int) {
//    override def apply(x: Int): Int = x * 2
//  }

  // an equivalent method
  val doubler = (x: Int) => x * 2
  // this instantiates a new Function1 with the override def apply which takes an X which is an Int and returns x * 2
  // this syntactic sugar is called an Anonymous function or a Lambda

  // aka
  // val doubler: Int => Int = (x: Int) => x * 2
  //  val doubler: Int => Int = x => x * 2

  // multiple params in a Lambda
  val adder = (a: Int, b: Int) => a + b
  // aka
  // val adder: (Int, Int) => Int = a + b

  // no params
  val noParam = () => 3
  // val noParam: () => Int = () => 3

  println(noParam) // function itself
  println(noParam()) // function call

  // curly braces with lambda

  val stringToInt = { (str: String) =>
    str.toInt
  }

  // more syntactic sugar
  // val niceInc: Int => Int = (x: Int) => x + 1
  val niceInc: Int => Int = _ + 1 // equivalent to x => x + 1

  // val niceAdder: (Int, Int) => Int = (a, b) => a + b
  val niceAdder: (Int, Int) => Int = _ + _ // equivalent to (a, b) => a + b
  // this notation is extremely useful in practice when you want to chain higher order function calls

  // NOTE: you can't use underscore multiple times as each underscore in an expressions represents only 1 element/param

  /*
  * 1. MyList: replace all FunctionX calls with Lambdas
  * 2. Rewrite the special adder as an anonymous function
  * */

  // Q2

//  val superAdder: (Int => (Int => Int)) = new (Int => (Int => Int)) {
//    override def apply(x: Int): (Int => Int) = new (Int => Int) {
//      override def apply(y: Int): Int = x + y
//    }
//  }

  val superAdder = (x: Int) => (y: Int) => x + y

  println(superAdder(5)(6))
}
