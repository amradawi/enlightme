package forms

/**
  *     val Id = "_id"
    val Question ="question"
    val Answer = "short_answer"
    val DetailedAnswer = "long_answer"
    val Difficulty = "difficulty"
    val Tags = "tags"
    val Rate = "rate"
  * Created by amradawi on 2017-03-21.
  */
case class QuestionForm(question: String, short_answer: String, long_answer: String,
                        difficulty: String, tags: List[String], rate:Int)


