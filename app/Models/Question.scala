package Models

import org.joda.time.DateTime

import play.api.data._
import play.api.data.Forms.{ text, longNumber, mapping, nonEmptyText, optional }
import play.api.data.validation.Constraints.pattern

/**
  * Created by amradawi on 2017-03-24.
  */
case class Question(
                   id: Option[String],
                   question: String,
                   shortAnswer: String,
                   longAnswer: String,
                   difficulty: String,
                   rate: String,
                   tags: String,
                   creationDate: Option[DateTime],
                   updateDate: Option[DateTime]
                   )


object Question {
  import play.api.libs.json._

  implicit object  QuestionWrites extends OWrites[Question]{
    def writes(question: Question): JsObject = Json.obj(
      "_id" -> question.id,
      "question" -> question.question,
      "shortAnswer" -> question.shortAnswer,
      "longAnswer" -> question.longAnswer,
      "difficulty" -> question.difficulty,
      "tags" -> question.tags,
      "rate" -> question.rate,
      "creationDate" -> question.creationDate.fold(-1L)(_.getMillis),
      "updateDate" -> question.updateDate.fold(-1L)(_.getMillis)
    )
  }
  implicit object QuestionReads extends Reads[Question]{
    def reads(json: JsValue): JsResult[Question] = json match {
      case jObj: JsObject =>
        try{
          val id = (jObj \ "_id").asOpt[String]
          val question = (jObj \ "question").as[String]
          val shortAnswer = (jObj \ "shortAnswer").as[String]
          val longAnswer = (jObj \ "longAnswer").as[String]
          val tags = (jObj \ "tags").as[String]
          val difficulty = (jObj \ "difficulty").as[String]
          val rate = (jObj \ "rate").as[String]
          val creationDate = (jObj \ "creationDate").asOpt[Long]
          val updateDate = (jObj \ "updateDate").asOpt[Long]

          JsSuccess(Question(id, question, shortAnswer,longAnswer,difficulty,rate, tags,
            creationDate.map(new DateTime(_)),
            updateDate.map(new DateTime(_))))

        }catch {
          case err: Throwable => JsError(err.getMessage)
        }
      case _ => JsError("not a valid Json Object")
    }
  }
  val form = Form(
    mapping(
      "id" -> optional(text verifying pattern(
        """[a-fA-F0-9]{24}""".r, error = "error.objectId")),
      "question" -> nonEmptyText,
      "shortAnswer"-> nonEmptyText,
      "longAnswer" -> text,
      "difficulty" -> text,
      "tags" -> nonEmptyText,
      "rate" -> text,
      "createionDate" -> optional(longNumber),
      "updateDate" -> optional(longNumber)){
      (id, question, shortAnswer, longAnswer, difficulty, tags, rate, creationDate, updateDate) =>
        Question(id, question,shortAnswer, longAnswer, difficulty, tags,rate,  creationDate.map(new DateTime(_)),
          updateDate.map(new DateTime(_)))
    }{ question => Some(
      (
      question.id,
      question.question,
      question.shortAnswer,
      question.longAnswer,
      question.difficulty,
      question.tags,
      question.rate,
      question.creationDate.map(_.getMillis),
      question.updateDate.map(_.getMillis)))
    })
}
