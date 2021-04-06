package lectures.part3_fp

import scala.annotation.tailrec

object TuplesMaps extends App {

  // Tuples are finite ordered "lists" (Like lists)
  val aTuple = new Tuple2(2, "Hello, Scala")
  // they basically group things together, the above e.g. is a tuple with an Int and a String
  // the type is Tuple2[Int, String] also known to Scala as (Int, String) - parenthesis = syntactic sugar
  // you can also create Tuples without the "new" keyword because Tuple has appropriate apply methods in their companion objects

  val anotherTuple = (3, "Hello, Scala") // syntactic sugar
  // Tuples can group at most 22 elements of different types as it works in conjunction with Function types where FunctionX can go from Function0 to Function22

  // accessing elements
  println(aTuple._1) // 1st element
  println(aTuple._2) // 2nd element
  println(aTuple.copy(_2 = "goodbye Java")) // similar to case classes
  println(aTuple.swap) // res -> ("Hello, Scala", 2)

  // Maps
  // collections that associate Keys to Values
  // Keys are the index and the data corresponding to these Keys are Values

  val aMap: Map[String, Int] = Map()

//  val phoneBook = Map(("Jim", 555), ("Daniel", 879))
  // you can populate a map by specifying Tuples/Pairings

  // syntactic sugar
  val phoneBook = Map("Jim" -> 555, "Daniel" -> 879, "JIM" -> 9000).withDefaultValue(-1)
  // to prevent crashes (when accessing a Key not present in the Map), add the withDefaultValue() method

  println(phoneBook)

  // map operations
  println(phoneBook.contains("Jim")) // checks if the Key exists - returns True
  println(phoneBook("Pam")) // returns the value associated to this Key (in this case 879)

  // add a pairing
  val newPairing = "Pam" -> 586
  val newPhoneBook = phoneBook + newPairing
  // create a new Map with the new pairing appended to the previous one as MAPS ARE IMMUTABLE
  println(newPhoneBook)

  // functions on maps: map, flatMap, filter

  println(phoneBook.map(pair => pair._1.toLowerCase -> pair._2)) // map() takes a pairing, so does flatMap() and so does filter()\

  // filterKeys & mapValues : widely used with Maps
  println(phoneBook.view.filterKeys(x => x.startsWith("J")).toMap)

  println(phoneBook.view.mapValues(n => n * 10).toMap)

  // conversions to other collections
  println(phoneBook.toList)
  println(List(("Daniel", 245)).toMap)

  val names = List("Kevin", "Angela", "Creed", "Andy", "Michael")
  println(names.groupBy(name => name.charAt(0)))

  /* EXERCISE
  *
  * 1. What would happen if I had two original entries "Jim" -> 555 and "JIM" -> 900 and ran .toLowerCase() ?
  *
  * Ans. the value assigned to the lower case "jim" is of the most recent overlapping key i.e. 9000 in this case
  * The takeaway is that avoid using overlapping keys as it might cause a potential loss of data
  *
  * 2. Overly simplified social network based on maps
  *   Person = String
  *   - add a person to the network
  *   - remove
  *   - friend (mutual)
  *   - unfriend
  *
  *   Stats
  *   - number of friends of a person
  *   - person with most friends
  *   - how many people have NO friends
  *   - if there is a social connection between two people (direct or not)
  * */

  // Q2.

  def add(network: Map[String, Set[String]], person: String): Map[String, Set[String]] = {
    network + (person -> Set())
  }

  def friend(network: Map[String, Set[String]], p1: String, p2: String): Map[String, Set[String]] = {
    val friendsP1 = network(p1)
    val friendsP2 = network(p2)

    network + (p1 -> (friendsP1 + p2)) + (p2 -> (friendsP2 + p1))
  }

  def unfriend(network: Map[String, Set[String]], p1: String, p2: String): Map[String, Set[String]] = {
    val friendsP1 = network(p1)
    val friendsP2 = network(p2)

    network + (p1 -> (friendsP1 - p2)) + (p2 -> (friendsP2 - p1))
  }

  def remove(network: Map[String, Set[String]], person: String): Map[String, Set[String]] = {
    @tailrec
    def removeHelper(friends: Set[String], networkAcc: Map[String, Set[String]]): Map[String, Set[String]] = {
      if(friends.isEmpty) networkAcc
      else removeHelper(friends.tail, unfriend(networkAcc, person, friends.head))
    }

    val unfriended = removeHelper(network(person), network)
    unfriended - person
  }

  val empty: Map[String, Set[String]] = Map()
  val network = add(add(empty, "Bob"), "Mary")
  println(network)
  println(friend(network, "Bob", "Mary"))
  println(unfriend(friend(network, "Bob", "Mary"), "Bob", "Mary"))
  println(remove(friend(network, "Bob", "Mary"), "Bob"))

  val people = add(add(add(empty, "Bob"), "Mary"), "Jim")
  val jimBob = friend(people, "Bob", "Jim")
  val testNet = friend(jimBob, "Bob", "Mary")

  println(testNet)

  def nFriends(network: Map[String, Set[String]], person: String): Int = {
    if(!network.contains(person))0
    else network(person).size
  }

  println(nFriends(testNet, "Bob"))

  def mostFriends(network: Map[String, Set[String]]): String = {
    network.maxBy(pair => pair._2.size)._1

    // maxBy() (specific to maps) receives a lambda from a pair to a value where the value has to be comparable
    // this returns the key-value pair with the maximum value given by the lambda
  }

  println(mostFriends(testNet))

  def nPeopleWithNoFriends(network: Map[String, Set[String]]): Int = {
//    network.view.filterKeys(k => network(k).isEmpty).size
//    network.count(pair => pair._2.isEmpty)
    network.count(_._2.isEmpty)
  }

  println(nPeopleWithNoFriends(testNet))

  def socialConnection(network: Map[String, Set[String]], p1: String, p2: String): Boolean = {
    @tailrec
    def bfs(target: String, visited: Set[String], notVisited: Set[String]): Boolean = {
      if(notVisited.isEmpty) false
      else {
        val person = notVisited.head

        if(person == target) true
        else if(visited.contains(person)) bfs(target, visited, notVisited.tail)
        else bfs(target, visited + person, notVisited.tail ++ network(person))
      }
    }
    bfs(p2, Set(), network(p1) + p1)
  }

  println(socialConnection(testNet, "Mary", "Jim"))
  println(socialConnection(network, "Mary", "Bob"))
}
