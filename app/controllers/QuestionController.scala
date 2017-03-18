package controllers

import javax.inject.Inject

import play.api.libs.json.Json
import repos.QuestionReposMongoImp
import play.api.mvc._
import play.modules.reactivemongo.{MongoController, ReactiveMongoApi, ReactiveMongoComponents}
import reactivemongo.bson.{BSONDocument, BSONObjectID}
import scala.concurrent._
import ExecutionContext.Implicits.global
/**
  * Created by amradawi on 2017-03-17.
  */
class QuestionController  @Inject()(val reactiveMongoApi: ReactiveMongoApi) extends Controller with MongoController with ReactiveMongoComponents{
  import QuestionFields._
  def questionsRepo = new QuestionReposMongoImp(reactiveMongoApi)

  def add_question = TODO

  def rate_question = TODO

  def tag_question = TODO

  def index = Action.async { implicit request =>
    questionsRepo.find().map(questions => Ok(Json.toJson(questions)))
  }

  def read(id: String) = Action.async {implicit request =>
      questionsRepo.select(BSONDocument(Id -> BSONObjectID(id))).map(question => Ok(Json.toJson(question)))
  }

  def delete(id: String) = Action.async {implicit request =>
    questionsRepo.remove(BSONDocument(Id -> BSONObjectID(id)))
    .map(result => Accepted)
  }

  def create = Action.async(BodyParsers.parse.json) { implicit request =>
    val question = (request.body \ Question).as[String]
    val answer = (request.body \ Answer).as[String]
    val detailedAnswer = (request.body \ DetailedAnswer).as[String]
    val difficulty = (request.body \ Difficulty).as[String]

    questionsRepo.save(BSONDocument(
      Question -> question,
      Answer -> answer,
      DetailedAnswer -> detailedAnswer,
      Difficulty -> difficulty
    )).map(result => Created)
  }

  object QuestionFields {
    val Id = "_id"
    val Question ="question"
    val Answer = "short_answer"
    val DetailedAnswer = "long_answer"
    val Difficulty = "difficulty"
  }

  def update(id: String) = TODO

}
