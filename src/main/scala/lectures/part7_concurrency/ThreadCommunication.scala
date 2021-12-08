package lectures.part7_concurrency

import scala.collection.mutable
import scala.util.Random

object ThreadCommunication extends App {
  /*
   * the producer-consumer problem
   *
   * producer -> [ x ] -> consumer
   * p: sets value inside the container
   * c: extracts value out of the container
   * p & c are running in parallel at the same time
   * */

  class SimpleContainer {
    private var value: Int = 0

    def isEmpty: Boolean = value == 0

    def set(newValue: Int) = value = newValue

    def get = {
      val result = value
      value = 0
      result
    }
  }

  def naiveProdCons(): Unit = {
    val container = new SimpleContainer

    val consumer = new Thread(() => {
      println("[consumer] waiting ...")

      while (container.isEmpty) {
        println("[consumer] actively waiting ...")
      }

      println("[consumer] I have consumed " + container.get)
    })

    val producer = new Thread(() => {
      println("[producer] computing ...")
      Thread.sleep(500)
      val value = 42
      println("[producer] I have produced " + value)
      container.set(value)
    })

    consumer.start()
    producer.start()
  }
//  naiveProdCons()

  // wait and notify
  def smartProdCons(): Unit = {
    val container = new SimpleContainer

    val consumer = new Thread(() => {
      println("[consumer] waiting ...")
      container.synchronized {
        container.wait()
        // at the point of waiting the consumer thread will release the lock on the container and
        // it will suspend until someone else, namely the producer will signal the container that they may continue
      }

      // container must have some value
      println("[consumer] I have consumed " + container.get)
    })

    val producer = new Thread(() => {
      println("[producer] hard at work ...")
      Thread.sleep(2000)
      val value = 42

      container.synchronized {
        println("[producer] I am producing " + value)
        container.set(value)
        container.notify()
      }
    })

    consumer.start()
    producer.start()
  }

//  smartProdCons()

  // complicated producer-consumer

  /*
   * producer -> [ ? ? ? ] -> consumer extracts any value that is new
   *
   * a caveat -> both the producer or consumer may block each other
   * e.g. the buffer is full  i.e the producer has produced enough values to fill the buffer
   * in that case, the producer must block until the consumer has finished extracting some values out and vice-verse,
   * i.e. if the consumer is fast and the buffer is empty, the consumer must block until the producer produces more
   * */

  def prodConsLargeBuffer(): Unit = {
    val buffer: mutable.Queue[Int] = new mutable.Queue[Int]
    val capacity = 3

    val consumer = new Thread(() => {
      val random = new Random()

      while (true) {
        buffer.synchronized {
          if (buffer.isEmpty) {
            println("[consumer] buffer empty, waiting ...")
            buffer.wait()
          }

          // there must be at least ONE value in the buffer
          val x = buffer.dequeue()
          println("[consumer] consumed " + x)

          buffer.notify()
        }

        Thread.sleep(random.nextInt(250))
      }
    })

    val producer = new Thread(() => {
      val random = new Random()
      var i = 0

      while (true) {
        buffer.synchronized {
          if (buffer.size == capacity) {
            println("[producer] buffer is full, waiting ...")
            buffer.wait()
          }

          // there must be at least ONE EMPTY SPACE in the buffer
          println("[producer] producing " + i)
          buffer.enqueue(i)

          buffer.notify()

          i += 1
        }

        Thread.sleep(random.nextInt(500))
      }
    })

    consumer.start()
    producer.start()
  }

//  prodConsLargeBuffer()

  /*
   * Prod-Cons level 3
   *
   * producer1 -> [ ? ? ?] -> consumer1
   * producer2 -> ... -> consumer2
   * */

  class Consumer(id: Int, buffer: mutable.Queue[Int]) extends Thread {
    override def run(): Unit = {
      val random = new Random()

      while (true) {
        buffer.synchronized {

          /*
           * producer produces value, two Cons are waiting
           * producer.notify() will notify one of the consumers ... afterwards, the consumer will dequeue and notify the buffer
           * the JVM will then notify the other consumer and it will try to dequeue which is WRONG!!!
           * */

          /* if(buffer.isEmpty) {
            println(s"[consumer $id] buffer empty, waiting ...")
            buffer.wait()
          }

          replacing the "if" block with a "while" block will help us tackle the above situation because
          if the thread is able to dequeue, it means the buffer is not empty AND the thread is awake. with "if"
          we were able to reach the dequeue point if the bugger was empty OR the thread was awake.

          A similar change will take place in the Producer class.
           */

          while (buffer.isEmpty) {
            println(s"[consumer $id] buffer empty, waiting ...")
            buffer.wait()
          }

          // there must be at least ONE value in the buffer
          val x = buffer.dequeue()
          println(s"[consumer $id] consumed " + x)

          // buffer.notify() no longer has the same semantics anymore because with the introduction of multiple prods and cons
          // it can notify a producer or another consumer and vice-versa
          buffer.notify()
        }

        Thread.sleep(random.nextInt(500))
      }
    }
  }

  class Producer(id: Int, buffer: mutable.Queue[Int], capacity: Int) extends Thread {
    override def run(): Unit = {
      val random = new Random()
      var i = 0

      while (true) {
        buffer.synchronized {
          while (buffer.size == capacity) {
            println(s"[producer $id] buffer is full, waiting ...")
            buffer.wait()
          }

          // there must be at least ONE EMPTY SPACE in the buffer
          println(s"[producer $id] producing " + i)
          buffer.enqueue(i)

          buffer.notify()

          i += 1
        }

        Thread.sleep(random.nextInt(500))
      }
    }
  }

  def multiProdCons(nConsumers: Int, nProducers: Int): Unit = {
    val buffer: mutable.Queue[Int] = new mutable.Queue[Int]
    val capacity = 3

    (1 to nConsumers).foreach(i => new Consumer(i, buffer).start())
    (1 to nProducers).foreach(i => new Producer(i, buffer, capacity).start())
  }

//  multiProdCons(3, 3)

  /*
   * Exercises:
   *
   * 1) come up with an example where notifyAll() acts in a different way than notify()
   * -> in the case of multi prods and cons, the behaviour will be the same for both because,
   * -> at every iteration inside either the producer or consumer, they're all synchronising on buffer
   * -> so even if there multiple prods and/or cons sleeping on buffer and we notifyAll(),
   * -> then at the next iteration when all threads are awake, they'll again all synchronise at the buffer i.e. all threads will be blocked except one
   *
   * 2) create a deadlock
   * 3) create a livelock (threads are active and not blocked but can't continue)
   *
   * */

  // Q1
  def testNotifyAll(): Unit = {
    val bell = new Object

    (1 to 10).foreach(i =>
      new Thread(() => {
        bell.synchronized {
          println(s"[thread $i] waiting ...")
          bell.wait()
          println(s"[thread $i] hooray")
        }
      }).start()
    )

    new Thread(() => {
      Thread.sleep(2000)
      println("Announcer rock-n-roll")
      bell.synchronized {
        bell.notifyAll()
//        bell.notify()

        /*
         * When using notify() in this case:
         * - 10 threads are initially waiting
         * - but after announcer, just one thread wakes up
         * - other 9 threads are still blocked and waiting to be woken up. This won't happen since notify() just awakens one thread
         * */
      }
    }).start()
  }

//  testNotifyAll()

  // Q2

  case class Friend(name: String) {
    def bow(other: Friend) = {
      this.synchronized {
        println(s"$this: I am bowing to my friend $other")
        other.rise(this)
        println(s"$this: my friend $other has risen")
      }
    }

    def rise(other: Friend) = {
      this.synchronized(println(s"$this: I am rising to my friend, $other"))
    }

    var side = "right"

    def switchSide() = {
      if (side == "right") side = "left"
      else side = "right"
    }

    def pass(other: Friend) = {
      while (this.side == other.side) {
        println(s"$this: please, $other, pass")
        switchSide()
        Thread.sleep(1000)
      }
    }
  }

  val f1 = Friend("f1")
  val f2 = Friend("f2")

//  new Thread(() => f1.bow(f2)).start()
//  new Thread(() => f2.bow(f1)).start()

  /*
   * The first thread blocks f1's lock as f1 bows to f2. Then subsequently blocks f2's lock
   * The second thread blocks f2's lock as f2 bows to f1. Then subsequent;y blocks f1's lock
   *
   * When the two threads act at the same time, they block their respective locks and then try to block each other's locks
   * which are at this point already locked
   *  */

  // Q3 - Livelock

  new Thread(() => f1.pass(f2)).start()
  new Thread(() => f2.pass(f1)).start()
}
