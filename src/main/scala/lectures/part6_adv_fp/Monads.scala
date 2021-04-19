package lectures.part6_adv_fp

object Monads extends App {

  // our own Try monad
  trait Attempt[+A] {
    def flatMap[B](f: A => Attempt[B]): Attempt[B]
  }

  object Attempt {
    def apply[A](a: => A): Attempt[A] =
      try {
        Success(a)
      } catch {
        case e: Throwable => Failure(e)
      }
    // we use a call-by-name argument coz we don't want it to be evaluated when we build the attempt
    // because the evaluation of the parameter might throw an exception and we want to prevent that
  }

  case class Success[+A](value: A) extends Attempt[A] {
    def flatMap[B](f: A => Attempt[B]): Attempt[B] =
      try {
        f(value)
      } catch {
        case e: Throwable => Failure(e)
      }

  }

  case class Failure(e: Throwable) extends Attempt[Nothing] {
    def flatMap[B](f: Nothing => Attempt[B]): Attempt[B] = this
  }

  /*
  * 1. left - identity
  * unit.flatMap(f) = f(x)
  *
  * Attempt(x).flatMap(f) = f(x) // Success case
  * Success(x).flatMap(f) = f(x) // proved
  *
  * 2. right - identity
  * attempt.flatMap(unit) = attempt
  *
  * Success(x).flatMap(x => Attempt(x)) => Attempt(x) = Success(x)
  * Failure(_).flatMap(...) => Failure(e)
  *
  * 3. associativity
  * attempt.flatMap(f).flatMap(g) == attempt.flatMap(x => f(x).flatMap(g))
  *
  * Failure(e).flatMap(f).flatMap(g) = Failure(e)
  * Failure(e).flatMap(x => f(x).flatMap(g)) = Failure(e)
  *
  * LHS. Success(v).flatMap(f).flatMap(g) = f(v).flatMap(g) OR Failure(e)
  * RHS. Success(v).flatMap(x => f(x).flatMap(g)) = f(v).flatMap(g) OR Failure(e)
  * LHS == RHS
  * */

  val attempt = Attempt {
    throw new RuntimeException("My own Monad")
  }

  println(attempt)

  /*EXERCISE:

  1. implement a Lazy[T] monad = computation which will only be executed when it's needed
    unit / apply
    flatMap

  2. Monads = unit + flatMap
     Monads = unit + map + flatten
     implement a map and flatten in terms of flatMap given a Monad[T]

     Monad[T] {
      def flatMap[B](f: T => Monad[B]): Monad[B] = ... implemented

      def map[B](f: T => B): Monad[B] = ???
      def flatten(m: Monad[Monad[T]]): Monad[T] = ???

      (~ List)
     }
  * */

  // 1. Lazy monad
  class Lazy[+A](value: => A) {
    // call by need
    private lazy val internalValue = value
    def use: A = internalValue
    def flatMap[B](f: (=> A) => Lazy[B]): Lazy[B] = f(internalValue)
  }

  object Lazy {
    def apply[A](value: => A): Lazy[A] = new Lazy(value) // unit
  }

  val lazyInstance = Lazy {
    println("lazy monad")
    42
  }

//  println(lazyInstance.use)

  val flatMapInstance = lazyInstance.flatMap(x => Lazy {
    x * 10
  })
  // this will print the "lazy monad" println() statement
  // i.e. the expression was executed even though we were only flatMapping the lazy monad

  // this happens because flatMapping on a lazy instance applies f() to the value
  // now since "value" is a by-name parameter, applying f() will evaluate it eagerly
  // this will evaluate the println() statement in the flatMapInstance

  // fix: we can change the function type and make it receive the parameter by name as well
  // this will delay the evaluation of value inside f() because f() receives the parameter by name

  val flatMapInstance2 = lazyInstance.flatMap(x => Lazy {
    x * 10
  })

  flatMapInstance.use
  flatMapInstance2.use

  // the above 2 LOC print twice meaning that the lazyInstance is evaluated twice
  // when operating with normal lazy values, we aren't evaluating continuously
  // we just evaluate them once and then use the already evaluated value

  // by implementing call-by-need, we evaluate once in the LOC flatMapInstance.use
  // and reuse the same evaluated value in the later call to flatMapInstance2.use

  /* MONAD LAWS
  *
  * Left-Identity
  * unit.flatMap(f) = f(v)
  * Lazy(v).flatMap(f) = f(v)
  *
  * Right-Identity
  * l.flatMap(unit) = l
  * Lazy(v).flatMap(x => Lazy(x)) = Lazy(v)
  *
  * associativity (l.flatMap(f).flatMap(g) = l.flatMap(x => f(x).flatMap(g))
  * Lazy(v).flatMap(f).flatMap(g) = f(v).flatMap(g)
  * Lazy(v).flatMap(x => flatMap(g)) = f(v).flatMap(g)
  * */

  // EXERCISE 2: map and flatten in terms of flatMap

  /*
  * Monad[T] { // List
      def flatMap[B](f: T => Monad[B]): Monad[B] = ... implemented

      def map[B](f: T => B): Monad[B] = flatMap(x => unit(f(x))) // Monad[B]
      def flatten(m: Monad[Monad[T]]): Monad[T] = m.flatMap((x: Monad[T]) => x)
     }
     *
     * List(1, 2, 3).map(_ * 2) = List(1, 2, 3).flatMap(x => List(
  * */
}
