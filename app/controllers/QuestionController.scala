package controllers

//import java.util.UUID
//import play.api.libs.json.{JsArray, JsObject, Json}
//
//
//import scala.concurrent._
//import ExecutionContext.Implicits.global
//
//import reactivemongo.api.Cursor
//import Models.Question
//
//import javax.inject.Inject
//
//import org.joda.time.DateTime
//
//import scala.concurrent.Future
//
//import play.api.i18n.{ I18nSupport, MessagesApi }
//import play.api.mvc.{ Action, Controller, Request }
//import play.api.libs.json.{ Json, JsObject, JsString }
//
////import reactivemongo.api.gridfs.{ GridFS, /*ReadFile*/ }
//import reactivemongo.play.json.collection.JSONCollection
//
//import play.modules.reactivemongo.{
//MongoController, ReactiveMongoApi, ReactiveMongoComponents
//}
//import play.modules.reactivemongo.json._, ImplicitBSONHandlers._
import javax.inject.Inject

import org.joda.time.DateTime

import scala.concurrent.{Await, Future}
import play.api.{Logger, db}
import play.api.Play.current
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{Action, Controller, Request}
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json.{JsObject, JsString, Json}
import reactivemongo.api.gridfs.{GridFS, ReadFile}
import play.modules.reactivemongo.{MongoController, ReactiveMongoApi, ReactiveMongoComponents}
import play.modules.reactivemongo.json._
import Models.Question
import play.modules.reactivemongo.json._
//import play.modules.reactivemongo.json.collection.JSONCollection
import play.api.mvc.Controller
import play.modules.reactivemongo._
import scala.concurrent.duration._
import reactivemongo.play.json.collection.JSONCollection

import scala.util.{Failure, Success}

/**
  * Created by amradawi on 2017-03-17.
  */
class QuestionController  @Inject()(val reactiveMongoApi: ReactiveMongoApi,
                                    val messagesApi: MessagesApi)
  extends Controller with MongoController with ReactiveMongoComponents{
  import java.util.UUID

  def collection = Await.result(reactiveMongoApi.database.map(_.collection[JSONCollection]("questions")), 10.seconds)

  val r = scala.util.Random

  def index = Action.async { implicit request =>
      // let's do our query
      val c = collection.
        find(Json.obj()).
        // perform the query and get a cursor of Question
        cursor[Question]

      // gather all the Questions in a list
      val futureQuestionsList: Future[List[Question]] = c.collect[List]()

      //Let's reply with the array
    futureQuestionsList.map { questions =>
        Ok(questions.toString())
      }
  }
//
//  def read(id: String) = Action.async {implicit request =>
//    val futureQuestion = collection.find(Json.obj("_id" -> id)).one[Question]
//    for{
//      question <- futureQuestion
//    }
//    Ok(question)
//  }

  def delete(id: String) = Action.async {implicit request =>
    val futureRemove = collection.remove(Json.obj("_id"->id))
    futureRemove.onComplete {
      case Failure(e) => Ok("failed")
      case Success(lasterror) => {
        Ok("successfully removed document")
      }}
      futureRemove.map(result => Accepted)
  }

  def create = Action.async { implicit request =>
    implicit val messages = messagesApi.preferred(request)

    Question.form.bindFromRequest.fold(
      errors => Future.successful(
        Ok(views.html.updateQuestion(None, errors))),

      // if no error, then insert the question and redirect to Front page
      question => collection.insert(question.copy(
        id = question.id.orElse(Some(UUID.randomUUID().toString)),
        creationDate = Some(new DateTime()),
        updateDate = Some(new DateTime()))
      ).map(_ => Redirect(routes.QuestionController.index))
    )
  }
//  def create = Action.async(BodyParsers.parse.json) { implicit request =>
//    val question = (request.body \ Question).as[String]
//    val answer = (request.body \ Answer).as[String]
//    val detailedAnswer = (request.body \ DetailedAnswer).as[String]
//    val difficulty = (request.body \ Difficulty).as[String]
//    val tags =  (request.body \ Tags).as[String]
//    val rate = (request.body \ Rate).as[Int]
//
//    questionsRepo.save(BSONDocument(
//      Question -> question,
//      Answer -> answer,
//      DetailedAnswer -> detailedAnswer,
//      Difficulty -> difficulty,
//      Tags -> tags,
//      Rate -> rate
//    )).map(result => Created)
//  }
//
  def update(id: String) = Action.async { implicit  request =>
    implicit val messages = messagesApi.preferred(request)
    import reactivemongo.bson.BSONDateTime
    Question.form.bindFromRequest.fold(
      errors => Future.successful(
        Ok(views.html.updateQuestion(Some(id), errors))),

      question => {
        // create a modifier document, ie a document that contains the update operations to run onto the documents matching the query
        val modifier = Json.obj(
          // this modifier will set the fields
          "$set" -> Json.obj(
            "updateDate" -> BSONDateTime(new DateTime().getMillis),
            "question" -> question.question,
            "shortAnswer" -> question.shortAnswer,
            "longAnswer" -> question.longAnswer,
            "rate" -> question.rate,
            "difficulty"-> question.difficulty,
            "tags" -> question.tags
))

        // ok, let's do the update
        collection.update(Json.obj("_id" -> id), modifier).
          map { _ => Redirect(routes.QuestionController.index) }
      })


//    val question = (request.body \ Question).as[String]
//    val answer = (request.body \ Answer).as[String]
//    val detailedAnswer = (request.body \ DetailedAnswer).as[String]
//    val difficulty = (request.body \ Difficulty).as[String]
//    val tags =  (request.body \ Tags).as[String]
//    val rate = (request.body \ Rate).as[Int]
//
//    questionsRepo.update(BSONDocument(Id -> BSONObjectID(id)), BSONDocument("$set" -> BSONDocument(
//      Question -> question,
//      Answer -> answer,
//      DetailedAnswer -> detailedAnswer,
//      Difficulty -> difficulty,
//      Tags -> tags,
//      Rate -> rate
//    ))).map(result => Accepted)
  }

//  def form = Action { implicit request =>
//    Ok(views.html.question(QuestionController.questionForm))
//  }
//
//  def get_random_question() = Action.async { implicit  request =>
//    questionsRepo.randomDocument().map(question => Ok(Json.toJson(question)))
//  }
//
//  def submit_question() = Action.async(parse.form(QuestionController.questionForm)) { implicit request =>
//    val questionData = request.body
//    questionsRepo.save(BSONDocument(
//      Question -> questionData.question,
//      Answer -> questionData.short_answer,
//      DetailedAnswer -> questionData.long_answer,
//      Tags -> questionData.tags,
//      Rate -> questionData.rate
//    )).map(result => Created)
//  }
//
//  def get_question_with_tag_id(tag: String) = Action.async {  implicit request =>
//      val like_tag = "/.*" + tag +  ".*/"
//      val geneircQueryBuilder = BSONDocument(Tags -> like_tag)
//      questionsRepo.find(geneircQueryBuilder).map(questions => Ok(Json.toJson(questions)))
//  }
//
//  def get_questions_with_difficulty(difficulty: String) = Action.async { implicit request =>
//    questionsRepo.find(BSONDocument(Difficulty -> difficulty)).map(question => Ok(Json.toJson(question)))
//  }
//
//  def get_questions_of_rate(rate: String) = Action.async { implicit request =>
//    questionsRepo.find(BSONDocument(Rate -> rate)).map(question => Ok(Json.toJson(question)))
//  }

//  object QuestionController {
//    val questionForm = Form(mapping(
//      "question" -> nonEmptyText,
//      "short_answer" -> nonEmptyText,
//      "long_answer" -> text,
//      "difficulty" -> nonEmptyText,
//      "tags" -> list(text),
//      "rate" -> number
//    )(QuestionForm.apply)(QuestionForm.unapply))
//
//  }
}