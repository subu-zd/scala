package lectures.part3_fp

import scala.util.Random

object Sequences extends App {
  val aSeq = Seq(1 ,2, 3, 4)
  println(aSeq) // This Seq is actually outputted as a List
  // the Seq companion object actually has an apply factory method that can construct subclasses of sequence
  // but the declared type of sequence in this case is Sequence of Int
  println(aSeq.reverse)
  println(aSeq(2))
  println(aSeq ++ Seq(7, 6, 5))
  println((aSeq ++ Seq(7, 6, 5)).sorted) // the sorted method works if the type by default is "ordered"

  // Ranges
  val aRange: Seq[Int] = 1 to 10 // "1 until 10" gets the numbers from the left end to the right end(not included) so 1 to 9 in this case
  aRange.foreach(x => print(x + " "))

  // Lists
  val aList = List(1, 2, 3)
  val prepend = 42 :: aList // to prepend an element to the list
  val prepend2 = 42 +: aList :+ 89 // prepend and append respectively
  println(prepend)
  println(prepend2)

  val apples5 = List.fill(5)("apple") // fill() is a curried function that takes a number and a value and generates a list of the supplied value of length number
  println(apples5)

  println(aList.mkString("->"))

  // arrays
  val nos = Array(1, 2, 3, 4)
  val threeElements = Array.ofDim[Int](3, 3)
  threeElements.foreach({ x => x.foreach(print); println })

  nos(2) = 0 // syntactic sugar for nos.update(2, 0)
  // the update() method is rarely used in practice and only used for mutable collections
  println(nos.mkString(" "))

  // arrays and seq
  val noSeq: Seq[Int] = nos // this is an implicit conversion
  // even though "nos" is an array totally unrelated to the type Sequence, the conversion can be applied
  println(noSeq)

  // vectors
  val vector: Vector[Int] = Vector(1, 2, 3)
  println(vector)

  // vectors vs list
  val maxRuns = 1000
  val maxCapacity = 1000000
  def getWriteTime(collection: Seq[Int]): Double = {
    val r = new Random
    val times = for {
      i <- 1 to maxRuns
    } yield {
      val currentTime = System.nanoTime()
      collection.updated(r.nextInt(maxCapacity), r.nextInt())
      System.nanoTime() - currentTime
    }

    times.sum * 1.0 / maxRuns
  }

  val numberList = (1 to maxCapacity).toList
  val numberVec = (1 to maxCapacity).toVector

  // 1. list has the property that it saves the reference to the tail, so if it tries to update the first element then the list is incredibly efficient
  // 2. but updating an element in the middle index won't be as efficient

  println(getWriteTime(numberList)) // 5-6M nano seconds

  // 1. vector on the other hand has to traverse the entire 32 branch tree and replace that entire chunk
  // the advantage is that the depth of the tree is small
  // 2. the disadvantage is that a vector needs to replace an entire 32 branch chunk
  println(getWriteTime(numberVec)) // 2-3K nano seconds (way faster and hence the default implementation of Seq[])
}
