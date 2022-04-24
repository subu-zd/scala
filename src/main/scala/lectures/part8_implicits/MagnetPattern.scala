package lectures.part8_implicits

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

object MagnetPattern extends App {
  // the magnet pattern is a use case of type classes which aims at solving some of the problems create by method overloading

  // mock peer-to-peer network

  class P2PRequest
  class P2PResponse
  class Serializer[T]

  trait Actor {
    def receive(statusCode: Int): Int
    def receive(request: P2PRequest): Int
    def receive(request: P2PResponse): Int
    def receive[T: Serializer](message: T): Int
    def receive[T: Serializer](message: T, statusCode: Int): Int
    def receive(future: Future[P2PRequest]): Int
    // lots of overloads
  }

  /*
   * 1 - type erasure
   * i.e. if we were to add another def receive(future: Future[P2PResponse]): Int, it will fail to compile,
   *   as generics are erased at compiler time. So after the type erasure, both of the methods would simply receive a future
   *
   * 2. if we want some high-order-functions, lifting doesn't work for all overloads
   * e.g.
   *    val receiveFV = receive _
   *   // the underscore could be a statusCode, a request, q response etc. (basically all the implemented overloads)
   *
   * 3. code duplication
   * i.e. chances are there will be some duplication as the fundamental logic might be similar across all overloaded methods
   *
   * 4. type inference and default args
   * e.g. actor.receive(?) // the default arg can be any of the implemented overloads
   * */

  trait MessageMagnet[Result] {
    def apply(): Result
  }

  def receive[R](magnet: MessageMagnet[R]): R = magnet()

  // How can we ensure that this "receive" method can possible somehow get some other types as arguments
  // ans: Implicit conversion to an Implicit class

  implicit class FromP2PRequest(request: P2PRequest) extends MessageMagnet[Int] {
    override def apply(): Int = {
      // logic for handling a P2P Request
      println("handling P2P request")
      42
    }
  }

  implicit class FromP2PResponse(response: P2PResponse) extends MessageMagnet[Int] {
    override def apply(): Int = {
      // logic for handling a P2P Request
      println("handling P2P response")
      24
    }
  }

  receive(new P2PRequest)
  // an implicit conversion happens here from P2PResponse into a MessageMagnet
  // which will be then passed on the receive method
  // which will then call the magnet
  // whose apply method just does our handling logic
  receive(new P2PResponse)

  // BENEFITS:
  // 1. no more type erasure problem

  implicit class FromResponseFuture(future: Future[P2PResponse]) extends MessageMagnet[Int] {
    override def apply(): Int = 2
  }

  implicit class FromRequestFuture(future: Future[P2PRequest]) extends MessageMagnet[Int] {
    override def apply(): Int = 3
  }

  println(receive(Future(new P2PResponse)))
  println(receive(Future(new P2PRequest)))
  // now this magnet pattern is applicable for future of different types
  // that is because the compiler looks for implicit conversions before the types are erased

  // 2. lifting

  trait MathLib {
    def add1(x: Int) = x + 1
    def add1(s: String) = s.toInt + 1
  }

  trait AddMagnet {
    def apply(): Int
  }

  def add1(magnet: AddMagnet): Int = magnet()

  implicit class AddInt(x: Int) extends AddMagnet {
    override def apply(): Int = x + 1
  }

  implicit class AddString(s: String) extends AddMagnet {
    override def apply(): Int = s.toInt + 1
  }

  val addFV = add1 _
  println(addFV(1))
  println(addFV("3"))

  // this works with a catch. We did not add a type parameter in the AddMagnet trait,
  // that is because if we did, the compiler would not have known for which type this lifted function applies to
  // in the above case, since the apply() method returns Int, it knows the result for the lifted function

  // DISADVANTAGES
  /*
   * 1. Super verbose
   * i.e. it has gone from method overloading to implementing a magnet, a receive method with type parameter, and implicit conversion
   *
   * 2. Harder to Read
   *
   * 3. you can't name or place default arguments
   * i.e. it can be Nothing
   * it has to receive some kind of magnet or something that converts to a magnet
   *
   * 4. call by name doesn't work correctly
   * (exercise: prove it!)
   * (hint: side effects)
   * */

  class Handler {
    def handle(s: => String): Unit = {
      println(s)
      println(s)
    }

    // other overloads
  }

  trait HandleMagnet {
    def apply(): Unit
  }

  def handle(magnet: HandleMagnet): Unit = magnet()

  implicit class StringHandle(s: => String) extends HandleMagnet {
    override def apply(): Unit = {
      println(s)
      println(s)
    }
  }

  def sideEffectMethod(): String = {
    println("Hello, Scala")
    "hahaha"
  }

//  handle(sideEffectMethod())
  handle {
    println("Hello, Scala") // this line won't be printed
    "hahaha"
  }
}
