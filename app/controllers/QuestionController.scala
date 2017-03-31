package controllers

import javax.inject.Inject

import org.joda.time.DateTime

import scala.concurrent.{Await, Future}
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.Action
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json.{JsObject, JsString, Json}
import play.modules.reactivemongo.{MongoController, ReactiveMongoApi, ReactiveMongoComponents}
import Models.Question
import play.modules.reactivemongo.json._
import reactivemongo.bson.BSONDocument
import play.api.mvc.Controller
import scala.concurrent.duration._
import reactivemongo.play.json.collection.JSONCollection

import scala.util.{Failure, Success}



/**
  * Questions Controller
  * Created by amradawi on 2017-03-17.
  */
class QuestionController  @Inject()(val reactiveMongoApi: ReactiveMongoApi,
                                    val messagesApi: MessagesApi)
  extends Controller with MongoController with ReactiveMongoComponents{
  import java.util.UUID

  /**
    *
    * Collection of questions
    */
  def collection = Await.result(reactiveMongoApi.database.map(_.collection[JSONCollection]("questions")), 10.seconds)

  /**
    * Front page should return all questions
    * @return
    */
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
        Ok(Json.toJson(questions))
      }
  }

  /**
    * Returan a record for a given ID
    * @param id
    * @return Json Object
    */
  def read(id: String) = Action.async {implicit  request =>
    implicit val messages = messagesApi.preferred(request)
    val futureRecord = collection.find(Json.obj("_id" -> id))
    futureRecord.cursor[Question].collect[List]().map{ q => Ok(Json.toJson(q))}
  }

  /**
    * Remove id from collection
    * @param id
    * @return
    */
  def delete(id: String) = Action.async {implicit request =>
    val futureRemove = collection.remove(Json.obj("_id"->id))
    futureRemove.onComplete {
      case Failure(e) => Ok("failed")
      case Success(lasterror) => {
        Ok("successfully removed document" + id)
      }}
      futureRemove.map(result => Accepted)
  }

  /**
    * Create a new Record in collection
    * @return
    */
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

  /**
    * Update a collection
    * @param id
    * @return
    */
  def update(id: String) = Action.async { implicit  request =>
    implicit val messages = messagesApi.preferred(request)
    import reactivemongo.bson.BSONDateTime
    Question.form.bindFromRequest.fold(
      errors => Future.successful(
        Ok(views.html.updateQuestion(Some(id), errors))),

      question => {
        // create a modifier document, i.e. a document that contains the update operations to run onto the documents matching the query
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
  }

  /**
    * Return questions that match tag
    * @param tag
    * @return
    */
  def questionWithTagId(tag: String) = Action.async {  implicit request =>
      val like_tag = "/.*" + tag +  ".*/"
      val c = collection.find(Json.obj("tags"-> like_tag)).cursor[Question]
      val futureQuestions = c.collect[List]()
      futureQuestions.map(questions => Ok(Json.toJson(questions)))
  }

  /**
    * Return questions that has this type of difficulty
    * @param difficulty
    * @return
    */
  def questionWithDifficultyLevel(difficulty: String) = Action.async {  implicit request =>
        val c = collection.find(Json.obj("difficulty"-> difficulty)).cursor[Question]
        val futureQuestions = c.collect[List]()
        futureQuestions.map(questions => Ok(Json.toJson(questions)))
  }

  /**
    * Return questions with the given rate
    * @param rate
    * @return
    */
  def questionWithRate(rate:String) = Action.async{ implicit request =>
    val c = collection.find(Json.obj("rate"-> rate)).cursor[Question]
    val futureQuestions = c.collect[List]()
    futureQuestions.map(questions => Ok(Json.toJson(questions)))
  }

  /**
    * Return random questions
    * @param query
    * @return
    */
  def getRandomQuestion(query: Int)= Action.async { implicit request =>
    implicit val messages = messagesApi.preferred(request)
    val c = collection.aggregate(collection.BatchCommands.AggregationFramework.Sample(query)).map(_.head[BSONDocument])
    c.map(questions => Ok(Json.toJson(questions)))
  }

}