package lectures.part7_concurrency

import scala.concurrent.{Await, Promise}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.DurationInt
import scala.util.{Failure, Random, Success} // important for Futures. Future's apply method requires implicits as a second argument list
// the compiler looks for the "global" val to inject it into the parameter list
// ExecutionContext essentially handles thread allocation for futures
import scala.concurrent.Future

object FuturesPromises extends App {
  def calculateMeaningOfLife: Int = {
    Thread.sleep(2000)
    42
  }

  val aFuture = Future {
    calculateMeaningOfLife
  } // (global) passed by the compiler
  // This creates a Future object i.e. a Future instance by calling the apply method from the companion object of the Future Trait
  // and inside we pass the expression we want to delegate to another thread.

  println(aFuture.value) // returns an Option[Try[Int]]
  // this is because "aFuture" is of type Future[Int] meaning that the thread will compute an Int at some point
  // .value returns an Option[Try[Int]] because it essentially handles the case of the Future failing (hence Try)
  // and accordingly handles a case of a Future not finishing in time therefore returning None (hence Option)

  println("Waiting on for the Future to complete")
  aFuture.onComplete {
    case Success(value)     => println(s"the meaning of life is $value")
    case Failure(exception) => println(s"Failed with $exception")
  } // onComplete returns Unit so it is used for side-effects

  /*
   * onComplete is called by some thread :
   * -> it may be the thread that created this callback
   * -> it may be the thread that ran the future
   * -> it may be some other thread, but we don't assume which thread actually executes this */

  Thread.sleep(3000)

  // mini social network
  case class Profile(id: String, name: String) {
    def poke(otherProfile: Profile) = {
      println(s"${this.name} poking ${otherProfile.name}")
    }
  }

  object SocialNetwork {
    val names = Map(
      "fb.id.1-zuck" -> "Mark",
      "fb.id.2-gates" -> "Bill",
      "fb.id.3-musk" -> "Elon"
    )

    val friends = Map(
      "fb.id.1-zuck" -> "fb.id.2-gates"
    )

    val random = new Random()

    // API
    def fetchProfile(id: String): Future[Profile] = Future {
      Thread.sleep(random.nextInt(300))
      Profile(id, names(id))
    }

    def fetchBestFriend(profile: Profile): Future[Profile] = Future {
      Thread.sleep(random.nextInt(400))
      val bfId = friends(profile.id)
      Profile(bfId, names(bfId))
    }
  }

  // client : mark poke bill
  val mark = SocialNetwork.fetchProfile("fb.id.1-zuck")
  mark.onComplete {
    case Success(markProfile) => {
      val bill = SocialNetwork.fetchBestFriend(markProfile)
      bill.onComplete {
        case Success(billProfile) => markProfile.poke(billProfile)
        case Failure(e)           => e.printStackTrace()
      }
    }
    case Failure(e) => e.printStackTrace()
  }

  Thread.sleep(2500)

  // functional composition of Futures
  // map, flatMap, filter

  val nameOnTheWall = mark.map(_.name)

  val marksBestFriend = mark.flatMap(profile => SocialNetwork.fetchBestFriend(profile))

  val zucksBestFriendRestricted = marksBestFriend.filter(profile => profile.name.startsWith("Z"))

  // for-comprehension

  for {
    mark <- SocialNetwork.fetchProfile("fb.id.1-zuck")
    bill <- SocialNetwork.fetchBestFriend(mark)
  } yield {
    mark.poke(bill)
  }

  Thread.sleep(2500)

  // fallbacks

  // used when a Future fails with an exception inside
  // we can recover the future by returning a safe well-defined output
  val unknownProfile = SocialNetwork.fetchProfile("unknown.id").recover { case e: Throwable =>
    Profile("fb.id.0-dummy", "Dummy ")
  }

  // for the case when we don't want to return a Profile itself but to fetch another profile from the social network
  // in the case of recoverWith() where the second future also throws an exception, then there's nothing one can do.
  // but in practice, recoverWith() is only used when there's a high probability that the second future won't throw an exception
  val fetchedProfile = SocialNetwork.fetchProfile("unknown.id").recoverWith { case e: Throwable =>
    SocialNetwork.fetchProfile("fb.id.3-musk")
  }

  // similar to recover() / recoverWith()
  val fallbackResult = SocialNetwork.fetchProfile("unknown.id").fallbackTo(SocialNetwork.fetchProfile("fb.id.3-musk"))
  /*
   * this creates a new future with the following logic:
   * -> if the original future succeeds, then its value will be used as the value of the resulting future
   * -> if it fails with an exception then the argument inside "fallbackTo()" will be run
   * -> the argument succeeds, then that value will be used
   * -> but if the argument also fails, then the exception of the first future will be contained in the failure of resulting future
   * */

  /* WHY WOULD YOU BLOCK ON FUTURES ???
   * for certain use cases/critical operations, it makes sense to block on a future like
   * - bank transfers
   * - financial transactions or
   * - anything that is transaction like in nature
   * */

  // online banking app
  case class User(name: String)
  case class Transaction(sender: String, receiver: String, amount: Double, status: String)

  object BankingApp {
    val name = "zenbank"

    def fetchUser(name: String): Future[User] = Future {
      Thread.sleep(500) // simulate fetching from the DB
      User(name)
    }

    def createTransaction(user: User, merchantName: String, amount: Double): Future[Transaction] = Future {
      Thread.sleep(1000) // simulate some processes
      Transaction(user.name, merchantName, amount, "Success")
    }

    def purchase(username: String, item: String, merchantName: String, cost: Double): String = {
      // fetch the user from the DB
      // create a transaction
      // WAIT for the transaction to finish

      val transactionStatusFuture = for {
        user <- fetchUser(username)
        transaction <- createTransaction(user, merchantName, cost)
      } yield transaction.status

      Await.result(transactionStatusFuture, 2.seconds) // implicit conversions -> pimp my library
    }
  }

  println(BankingApp.purchase("Sub", "iPhone", "apple", 1000))
  // in this case, we didn't have to add Sleep calls in the main thread because this call will block until all futures are completed
  // however, it is recommended to use async computation and functional operators
  // use Await when it is necessary to block until a future is complete
  // Await.ready() attains a similar result but instead of the result within, it returns the same future as ready

  // NOTE: Await.result() and Await.ready() operate on the principle that if the duration here has passed, it will throw a timeout exception.
  // this is how we block futures

  // MANUAL MANIPULATION OF FUTURES WITH PROMISES

  /* Futures are ready only when they're done
   * but sometimes we need to specifically set or complete a future at a point of our choosing which is need that introduces the concept of PROMISE
   * */

  val promise = Promise[Int]() // promise is a "controller" over a future
  // promise has a member called Future, which it holds and manages
  val future = promise.future

  // thread 1 -> consumer
  future.onComplete { case Success(value) =>
    println("[consumer] I've received " + value)
  }

  // thread 2 - producer
  val producer = new Thread(() => {
    println("[producer] crunching numbers ...")
    Thread.sleep(500)
    // fulfilling the promise
    promise.success(42)
    /* The above line essentially manipulates the internal future to complete with a successful value of 42,
     * which is then in the onComplete() block of code by some consumer thread
     *
     * Alternatively you can fail by using :
     * promise.failure() with an exception as argument to the failure() method */

    println("[producer] done")
  })

  producer.start()
  Thread.sleep(1000)

  /*
   * 1) fulfill a future IMMEDIATELY with a value
   * 2) inSequence(fa, fb) - this function will run future B after it's made sure that future A is completed
   * 3) first(fa, fb) - return a future containing the earliest value returned by 2 value
   *    this future will return value of fa if it finished first or fb
   * 4) similar to 3) but with the last(fa, fb)
   * 5) run an action repeatedly until a condition is met and return the first value which satisfies the condition
   *    retryUntil[T](action: () => Future[T], condition: T => Boolean): Future[T]
   * */

}
