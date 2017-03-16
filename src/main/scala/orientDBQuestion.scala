package schema

import java.util.UUID

import scala.collection.JavaConverters._
import scala.collection.JavaConversions._
import javax.persistence.{Id, Version}

class User {
  @Id var id: String = _
  var name: String = _
  @Version var version: String = _

  override def toString = "User: " + this.id + ", name: " + this.name
}


class Question {
  @Id var id: String = _
  var title: String = _
  var user: User = _
  var answer: Answer = _
  @Version var version: String = _

  override def toString = "Question: " + this.id + ", title: " + this.title + ", asked by: " + user.name
}


class Answer{
  @Id var id: UUID = _
  var answer: String = _
  var source: String = _
  @Version var version: String = _

  override def toString = "Answer: " + this.answer + ", is provided from source " + this.source
}

class Tag{
  @Id var id: UUID = _
  var name: String = _
  @Version var version: String = _
  
}