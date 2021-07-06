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

      while(container.isEmpty) {
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
        // at the point of waiting the producer thread will release the lock on the container and
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

      while(true) {
        buffer.synchronized {
          if(buffer.isEmpty) {
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

      while(true) {
        buffer.synchronized {
          if (buffer.size == capacity) {
            println("[producer] buffer is full, waiting ...")
            buffer.wait()
          }

          // there must be at least ONE EMPTY SPACE in the buffer
          println("[producer] producing " + i)
          buffer.enqueue(i)

          buffer.notify()

          i+=1
        }

        Thread.sleep(random.nextInt(500))
      }
    })

    consumer.start()
    producer.start()
  }

  prodConsLargeBuffer()

  /*
  * Prod-Cons level 3
  *
  * producer1 -> [ ? ? ?] -> consumer1
  * producer2 -> ... -> consumer2
  * */

  class Consumer(id: Int, buffer: mutable.Queue[Int]) extends Thread {
    override def run(): Unit = {
      val random = new Random()

      while(true) {
        buffer.synchronized {

          /*
          * producer produces value, two Cons are waiting
          * producer.notify() will notify one of the consumers ... afterwards, the consumer will dequeue and notify the buffer
          * the JVm will then notify the other consumer and it will try to dequeue which is WRONG!!!
          * */

          if(buffer.isEmpty) {
            println(s"[consumer $id] buffer empty, waiting ...")
            buffer.wait()
          }

          // there must be at least ONE value in the buffer
          val x = buffer.dequeue()
          println(s"[consumer $id] consumed " + x)

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

      while(true) {
        buffer.synchronized {
          if (buffer.size == capacity) {
            println(s"[producer $id] buffer is full, waiting ...")
            buffer.wait()
          }

          // there must be at least ONE EMPTY SPACE in the buffer
          println(s"[producer $id] producing " + i)
          buffer.enqueue(i)

          buffer.notify()

          i+=1
        }

        Thread.sleep(random.nextInt(500))
      }
    }
  }
 }
