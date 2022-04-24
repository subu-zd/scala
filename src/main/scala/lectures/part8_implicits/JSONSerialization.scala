package lectures.part8_implicits

import java.util.Date

object JSONSerialization extends App {
  /*
   * Users, posts, feeds
   * Serialize to JSON
   * */

  case class User(name: String, age: Int, email: String)
  case class Post(content: String, createdAt: Date)
  case class Feed(user: User, posts: List[Post])

  /*
   * 1. Create intermediate data types - Int, String, List, Date
   * 2. Type Classes for conversion to intermediate data types
   * 3. Serialize to JSON
   * */

  sealed trait JSONValue {
    def stringify: String
  }

  final case class JSONString(value: String) extends JSONValue {
    def stringify: String = "\"" + value + "\""
  }

  final case class JSONNumber(value: Int) extends JSONValue {
    def stringify: String = value.toString
  }

  final case class JSONArray(value: List[JSONValue]) extends JSONValue {
    def stringify: String = value.map(_.stringify).mkString("[", ",", "]")
  }

  final case class JSONObject(values: Map[String, JSONValue]) extends JSONValue {
    /*
     * {
     *   name: "John"
     *   age: 22
     *   friends: []
     *   latestPost: {
     *     content: "Scala ricks"
     *     date: 20/04/2022
     *   }
     * }
     * */

    def stringify: String = values
      .map { case (key, value) =>
        "\"" + key + "\":" + value.stringify
      }
      .mkString("{", ",", "}")
  }

  val data = JSONObject(
    Map(
      "user" -> JSONString("Daniel"),
      "posts" -> JSONArray(
        List(
          JSONString("Scala rocks"),
          JSONNumber(453)
        )
      )
    )
  )

  println(data.stringify)

  // type class to convert User, Post, Feed to some implementation of JSONValue
  /*
   * 1. type class
   * 2. type class instances (implicit)
   * 3. pimp library to use type class instances
   * */

  // 2.1
  trait JSONConverter[T] {
    def convert(value: T): JSONValue
  }

  // 2.2
  implicit object StringConverter extends JSONConverter[String] {
    def convert(value: String): JSONValue = JSONString(value)
  }

  implicit object NumberConverter extends JSONConverter[Int] {
    def convert(value: Int): JSONValue = JSONNumber(value)
  }

  // custom data types
  implicit object UserConverter extends JSONConverter[User] {
    def convert(user: User): JSONValue = JSONObject(
      Map(
        "name" -> JSONString(user.name),
        "age" -> JSONNumber(user.age),
        "email" -> JSONString(user.email)
      )
    )
  }

  implicit object PostConverter extends JSONConverter[Post] {
    def convert(post: Post): JSONValue = JSONObject(
      Map(
        "content" -> JSONString(post.content),
        "created" -> JSONString(post.createdAt.toString)
      )
    )
  }

//  implicit object FeedConverter extends JSONConverter[Feed] {
//    def convert(feed: Feed): JSONValue = JSONObject(
//      Map(
//        "user" -> UserConverter.convert(feed.user), // TODO
//        "posts" -> JSONArray(feed.posts.map(PostConverter.convert)) // TODO
//      )
//    )
//  }
  // the above block can be improved as seen below

  // 2.3
  implicit class JSONEnrichment[T](value: T) {
    def toJSON(implicit converter: JSONConverter[T]): JSONValue =
      converter.convert(value)
  }

  // having defined the JSONEnrichment above, we can relinquish the dependency on a specific implementation of converter
  // this way the compiler will fetch whatever implicit representation is appropriate for the case
  implicit object FeedConverter extends JSONConverter[Feed] {
    def convert(feed: Feed): JSONValue = JSONObject(
      Map(
        "user" -> feed.user.toJSON,
        "posts" -> JSONArray(feed.posts.map(_.toJSON))
      )
    )
  }

  // call stringify on the result

  val now = new Date(System.currentTimeMillis())
  val john = User("John", 34, "john@rockthejvm.com")
  val feed = Feed(
    john,
    List(
      Post("hello", now),
      Post("look at this cute puppy", now)
    )
  )

  println(feed.toJSON.stringify)
}
