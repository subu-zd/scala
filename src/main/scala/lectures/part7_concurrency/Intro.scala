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

  threadPool.execute(() => {
    Thread.sleep(1000)
    println("done after 1 second")
  })

  threadPool.execute(() => {
    Thread.sleep(1000)
    println("almost done...")
    Thread.sleep(1000)
    println("done after 2 seconds")
  })

  //threadPool.shutdown() // shuts down all the threads in the thread pool.
  // shutting down means no more actions can be submitted
  //threadPool.execute(() => println("should not appear")) // throws an exception since the pool was already shut down

  //threadPool.shutdownNow() // this interrupts sleeping threads currently running under the pool
  println(threadPool.isShutdown)
}
