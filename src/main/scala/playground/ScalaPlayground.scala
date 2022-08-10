package playground

import java.sql.Timestamp
import java.time.{Instant, LocalDateTime}
import java.time.temporal.ChronoUnit
import scala.math.Ordered.orderingToOrdered

object ScalaPlayground extends App {

  case class Job(
    resourceMultiplier: Int,
    category: String = "XS4"
  )

  val j1 = Job(2, "XS4,XS3,XXS,M,L")
  val j2 = Job(8, "XXS,M,L")
  val j3 = Job(4, "XL,S,M,L")
  val j4 = Job(5, "XXS,M,L,XXL")
  val j5 = Job(5, "S,M,L")

  case class Zob(name: String)

  val z1 = Zob("z1")
  val z2 = Zob("z2")
  val z3 = Zob("z3")
  val z4 = Zob("z4")
  val z5 = Zob("z5")
  val z6 = Zob("z6")
  val z7 = Zob("z7")
  val z8 = Zob("z8")

  val l = List(j1, j2, j3, j4, j5)
  println(l.maxBy(_.resourceMultiplier))

  println(List(1, 2, 3) ++ List(5))

  val list1 = List(1, 2, 3, 4, 5, 6, 7)
  println(list1.filter(_ % 2 != 0))

  val (x, y) = list1.splitAt(list1.indexOf(2))
  println(x + " - " + y)

  val a :: b = List(1, 2, 3, 4, 5, 6, 7)
  println(a + " :: " + b)

  List(1, 2, 3).map(_ * 2)

  val oddNumbers: List[Int] = List(1, 3, 5, 7, 9, 11, 13)

//  def reduce[A1 >: Int](op: (A1, A1) => A1): A1

  def additionReduce(number1: Int, number2: Int): Int = {
    number1 + number2
  }

  println(oddNumbers.reduceRight(additionReduce))

  def instantIsBefore(maybeInstant: Option[Instant], age: Int, unit: ChronoUnit): Boolean = {
    val threshold = Instant.now.minus(age, unit)

    maybeInstant match {
      // check if the execution is finished more than X minutes ago
      // to make sure all λ functions triggered by that execution are finished as well
      case Some(stopDate) => stopDate.isBefore(threshold)
      case None           => false
    }
  }

  val temp = Timestamp.from(Instant.now.minus(120, ChronoUnit.MINUTES).truncatedTo(ChronoUnit.SECONDS))

  println(instantIsBefore(Some(temp.toInstant), 5400000, ChronoUnit.MILLIS))
  println(Timestamp.from(Instant.now.minus(2, ChronoUnit.HOURS).truncatedTo(ChronoUnit.SECONDS)))

  println(List(1, 1, 1, 5, 5, 5, 2, 3, 4, 5).filter(x => Seq(6, 5).contains(x)))

  val timeBool = Timestamp.from(Instant.now.minus(15, ChronoUnit.MINUTES)) <= Timestamp.from(Instant.now.minus(14, ChronoUnit.MINUTES))

  println("bool: " + timeBool)
  println(Timestamp.from(Instant.now.minus(15, ChronoUnit.MINUTES)))
  println(Timestamp.from(Instant.now.minus(14, ChronoUnit.MINUTES)))

  val testOpt1: Option[Job] = Some(Job(4, "XS3"))
  val testOpt2: Option[Job] = None

  println(testOpt1.map(_.resourceMultiplier).getOrElse(1))
  println(testOpt1.map(_.category).getOrElse("XS4"))
  println(testOpt2.map(_.resourceMultiplier).getOrElse(1))
  println(testOpt2.map(_.category).getOrElse("XS4"))

  println(List(1, 1, 1, 5, 5, 5, 2, 3, 4, 5).flatMap(a => List(a * 2)))

//  val str: String = "2021-12-07 08:03:12.3278569"
//  val dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SX")
//  val testTime = LocalDateTime.parse(str, dtf)
//  println(testTime)
  println(LocalDateTime.now.toLocalDate)

  val cancelReloadList = Seq(
    (1, "abc", "def", true),
    (1, "abc", "def", true),
    (1, "abc", "def", true),
    (1, "abc", "def", true),
    (1, "abc", "def", true)
  )
//    .map(a => (a._1, s"\"${ a._2 }\"", s"\"${ a._3 }\"", true))

  val tryOpt: Option[Int] = Some(1234)
  val tryOpt2: Option[Int] = None

  println(cancelReloadList.map(a => (a._1, s"\'${a._2}\'", s"\'${a._3}\'", true)).mkString(", "))

  println(tryOpt.getOrElse("").toString)
  println(tryOpt2.getOrElse("").toString)

  println(LocalDateTime.now.toString.substring(0, 19).replace("T", " "))

  println(123123123123L)

  val taskAccountCountMap = Map(
    "CacheLoad" -> 1,
    "SellLoad" -> 5
  )

  println(taskAccountCountMap.getOrElse("CacheLoad", 0))
  println(taskAccountCountMap.getOrElse("Cache", 0))

  println(taskAccountCountMap.get(""))

  println(cancelReloadList.map(_._1))

  println(List(1, 2, 3) ::: List(4, 5, 6))

  println(oddNumbers.mkString(", "))

  val sampleKVTupleList = List(
    (1234, "shard9"),
    (134685, "shard8"),
    (1234, "shard2"),
    (12937, "rscluster2")
  )

  val emptyList: List[(Long, String)] = List()

  println(emptyList.groupBy(_._1).view.mapValues(_.map(_._2)).toMap)

  println(List(1, 2).mkString(","))

  val accountsStringList = if (List().isEmpty) "0" else oddNumbers.mkString(", ")

  val query =
    s"""
       |SELECT j.account_id,
       |(CASE
       |WHEN l.location_type='Postgres' THEN l.postgres_cluster
       |WHEN l.location_type='Redshift' THEN l.redshift_cluster
       |END) AS datastore
       |FROM jobs j JOIN locations l ON j.write_location_id = l.id
       |WHERE j.account_id IN ($accountsStringList)
       |GROUP BY j.account_id, l.location_type, l.postgres_cluster, l.redshift_cluster;
       """.stripMargin

  println(query)

  val taskAppMap = Map(
    z1 -> List(j1, j2, j3),
    z2 -> List(j2, j5),
    z3 -> List(j1, j4, j5)
  )

  println(taskAppMap.values.toList.flatten)

  println(taskAppMap.filter(_._2.contains(3)).keys.mkString(","))

  println(List("a,b", "c", "b,d", "a,c,d").mkString(","))

  val listString = List("a,b", "c", "b,d", "a,c,d").mkString(",").split(",").toSet.toList
  println(listString)

  println(Seq(123, 245, 357).mkString(","))

  val l2 = Seq(j1, j2, j3, j4, j5)
  val longList = Seq(123L, 135L, 1355L)

  val categoryString = l2.map(_.category.trim).mkString(",").split(",")
  val uniqueCategory = categoryString.toSeq.distinct

  println(uniqueCategory.length)

  println(taskAppMap.filter(_._2.contains(j2)).keys.mkString(","))

  val uniqueDataLoadTasks = l2
    .flatMap { app =>
      taskAppMap.filter(_._2.contains(app)).keys.toSeq
    }
    .map(_.toString)
    .distinct

  println(uniqueDataLoadTasks)

  val smartAppMap1 = Map(
    z1 -> List(1, 2, 3),
    z2 -> List(2, 5),
    z3 -> List(1, 4, 5),
    z4 -> List(6, 8),
    z5 -> List(6, 7)
  )

  val smartAppMap2 = Map(
    z1 -> List(8, 5),
    z4 -> List(6, 8),
    z5 -> List(6, 7),
    z2 -> List(9)
  )

  println("smart apps: " + smartAppMap1.filter(_._2.contains(5)).values.flatten.filter(_ != 5))

  def helper(map: Map[Zob, List[Int]], appList: Set[Int], overlapList: Set[Int], coveredList: Set[Int]): Set[Int] = {
    val coveredAcc = appList ++ coveredList
    val overlapAcc: Set[Int] = appList
      .flatMap { app =>
        map
          .filter(_._2.contains(app))
          .values
          .flatten
          .filterNot(coveredAcc.contains)
      }

    println("appList: " + appList + ", overlapAcc: " + overlapAcc + ", overlapList: " + overlapList + ", coveredList: " + coveredList)

    if (overlapAcc.isEmpty) overlapList
    else helper(map, overlapAcc, overlapList ++ overlapAcc, coveredAcc)
  }

  println(helper(smartAppMap1, List(1, 2, 3).toSet, Set(), Set()))

  def queryTest(accountIds: Seq[Long] = Seq(), dataLoadTasks: Seq[String]) = {
    val accountIdFilterQuery =
      if (accountIds.isEmpty) ""
      else s"AND account_id IN (${accountIds.mkString(",")})"
    val dataLoadTaskValues = dataLoadTasks.map(task => s"\'$task\'").mkString(",")

    val query = s"""
                   |WITH accounts_and_tasks_with_read_locations AS (
                   |  SELECT distinct(account_id), read_location_id, task FROM jobs
                   |  INNER JOIN accounts ON accounts.id = jobs.account_id
                   |  WHERE task::text IN ($dataLoadTaskValues) $accountIdFilterQuery
                   |)
                   |INSERT INTO reload_requests (account_id, location_id, task)
                   |SELECT account_id, read_location_id, task
                   |FROM accounts_and_tasks_with_read_locations;
       """.stripMargin.trim

    query
  }

  println(queryTest(Seq(8523972L, 9915403L), Seq("BrandsLoad", "SellDealsLoad")))
  println(queryTest(dataLoadTasks = Seq("BrandsLoad", "SellDealsLoad")))

  println(l.sortBy(_.resourceMultiplier)(Ordering[Int].reverse))
}
