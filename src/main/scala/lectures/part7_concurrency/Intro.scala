package lectures.part7_concurrency

import java.util.concurrent.Executors

object Intro extends App {

  /*
  * interface Runnable {
  *   public void run()
  * }
  * */

  // JVM Threads
  val runnable = new Runnable {
    override def run(): Unit = println("Running in parallel")
  }

  val aThread = new Thread(runnable)
  // the Thread constructor takes an instance of a trait called Runnable as a Java Interface

  // creates a JVM thread which runs on top of an Operating System thread
  aThread.start() // gives a signal to the JVM to start a JVM thread.
  // the JVM then makes the thread and invokes the run method inside its inner Runnable

  // NOTE: There is a very clear distinction between a thread instance (aThread in this case) which is an object we operate on i.e. we call methods on it
  // and JVM thread which is the actual place where the parallel code is supposed to run

  // COMMON MISCONCEPTION:
  // If you want to execute some code in parallel you need to call the start() method on a thread instance
  // not the run() method on a Runnable
  // e.g. runnable.run() doesn't do anything in parallel

  aThread.join() // this call will block until the thread has finished running
  // this is a way to ensure that the thread has already run before you continue some computation

  val threadHello = new Thread(() => (1 to 5).foreach(_ => println("Hello")))
  val threadGoodbye = new Thread(() => (1 to 5).foreach(_ => println("Goodbye")))

  threadHello.start()
  threadGoodbye.start()
  // different runs in a multi-threaded environment produce different results
  // thread scheduling depends on a number of factors including OS and JVM

  // executors
  val threadPool = Executors.newFixedThreadPool(10)
  threadPool.execute(() => println("Something in the thread pool"))
  // the runnable passed will be executed by one of the 10 threads managed by this thread pool

//  threadPool.execute(() => {
//    Thread.sleep(1000)
//    println("done after 1 second")
//  })
//
//  threadPool.execute(() => {
//    Thread.sleep(1000)
//    println("almost done...")
//    Thread.sleep(1000)
//    println("done after 2 seconds")
//  })

  //threadPool.shutdown() // shuts down all the threads in the thread pool.
  // shutting down means no more actions can be submitted
  //threadPool.execute(() => println("should not appear")) // throws an exception since the pool was already shut down

  //threadPool.shutdownNow() // this interrupts sleeping threads currently running under the pool
  println(threadPool.isShutdown)

  def runInParallel = {
    var x = 0

    val thread1 = new Thread(() => {
      x = 1
    })

    val thread2 = new Thread(() => {
      x = 2
    })

    thread1.start()
    thread2.start()
    println(x)
  }

//  for(_ <- 1 to 10000) runInParallel
  // race condition -> two threads are attempting to access and change the same memory zone at the same time

  class BankAccount(@volatile var amount: Int) {
    override def toString: String = "" + amount
  }

  def buy(account: BankAccount, thing: String, price: Int): Unit = {
    account.amount -= price
//    println("I've bought " + thing)
//    println("My Account is now " + account)
  }

//  for(_ <- 1 to 10000) {
//    val account = new BankAccount(50000)
//    val thread1 = new Thread(() => buy(account, "shoes", 300))
//    val thread2 = new Thread(() => buy(account, "iPhone 12", 1000))
//
//    thread1.start()
//    thread2.start()
//    Thread.sleep(10)
//
//    if (account.amount != 48700) println("AHA: " + account.amount)
//  }
//    println()

    /*
    * thread1(shoes): 50000
    *   - account = 50000 - 300 = 49700
    * thread2(iphone): 50000
    *   - account = 50000 - 1000 = 49000 overwrites the memory of account.amount
    * */

    // option 1: use synchronized()
    def buySafe(account: BankAccount, thing: String, price: Int) = {
      account.synchronized {
        // no two threads can evaluate the expression, passed into the synchronized() method as parameter, at the same time
        account.amount -= price
        println("I've bought " + thing)
        println("My Account is now " + account)
      }
    }

    // option 2: use an annotation called @volatile
    // @volatile annotated on val or var means that all reads and writes to it are synchronized

    // out of the two options, the more powerful and used is the synchronized() method
    // because it allows you to put in more expressions in the same synchronized block
    // give you more control as to what you want to synchronize or isolate between threads


    /* EXERCISE
    *
    * 1) Construct 50 "inception" threads
    *   Thread1 -> thread2 -> thread 3 -> ...
    *   println("hello from thread #1")
    *   in reverse order
    * */

    def inceptionThreads(maxThreads: Int, i: Int = 1): Thread = new Thread(() => {
      if(i < maxThreads) {
        val newThread = inceptionThreads(maxThreads, i + 1)
        newThread.start()
        newThread.join()
      }
      println("Hello from thread " + i)
    })

    inceptionThreads(20).start()

    /* EXERCISE
    *
    * 2) */

    var x = 0
    val threads = (1 to 100).map(_ => new Thread(() => x += 1))
    threads.foreach(_.start())

    /*
    * 1) what is the biggest value possible for x? 100 (if all threads run sequentially)
    * 2) what is the smallest value possible for x? 1 (if all threads run in parallel and have x = 0 -> x + 1 = 1)
    */

    /*
    * 3) sleep fallacy
    * it describes the very wrong programming practice of synchronizing threads by putting them to sleep at different times
    * */
    var message = ""
    val awesomeThread = new Thread(() => {
      Thread.sleep(1000)
      message = "Scala is awesome"
    })

    message = "Scala sucks"
    awesomeThread.start()
    Thread.sleep(2000)
    println(message)

    /*
    * what's the value of message -> almost always is "Scala is awesome"
    * is it guaranteed? -> not guaranteed
    * why? or why not? ->
    *
    * (main thread)
    *   message = "Scala sucks"
    *   awesomeThread.start()
    *   sleep() - relives execution
    *
    * (awesome thread)
    *   sleep() - relives execution
    *
    * (OS gives the CPU to some important thread - takes the CPU for more than 2 seconds)
    *
    * Now after the important thread has finished running, the OS is very generous and get back to our program
    * but at this point, both the MAIN THREAD and AWESOME THREAD have finished sleeping
    * and the OS is free to choose which thread to run and it decides to give the CPU to the main thread and not the awesomeThread
    *
    *   println("Scala sucks")
    *
    * (OS gives the CPU to awesomeThread)
    *   message = "Scala is awesome" // but by this time its too late since the println() statement has already finished executing
    *
    * NOTE 1: Sleeping doesn't guarantee that a thread will sleep exactly for that number of milliseconds
    *         it will just yield the execution of the CPU to the OS for at least that number of milliseconds
    *
    * NOTE 2: Sleeping doesn't guarantee the ordering of evaluation of expressions
    *
    * */

    // how do we fix this?
    // synchronizing doesn't work here since it is only useful for CONCURRENT MODIFICATIONS
    // the solution is awesomeThread.join() and ensure it is finished before printing

}
