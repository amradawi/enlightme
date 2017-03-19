package controllers

import javax.inject.Inject

import play.api.libs.json.Json
import repos.QuestionReposMongoImp
import play.api.mvc._
import play.modules.reactivemongo.{MongoController, ReactiveMongoApi, ReactiveMongoComponents}
import scala.concurrent._
import ExecutionContext.Implicits.global
import play.modules.reactivemongo.json._
import play.modules.reactivemongo.json.collection.JSONCollection
import reactivemongo.bson.{BSONDocument, BSONObjectID}

/**
  * Created by amradawi on 2017-03-17.
  */
class QuestionController  @Inject()(val reactiveMongoApi: ReactiveMongoApi) extends Controller with MongoController with ReactiveMongoComponents{
  import QuestionFields._


  object QuestionFields {
    val Id = "_id"
    val Question ="question"
    val Answer = "short_answer"
    val DetailedAnswer = "long_answer"
    val Difficulty = "difficulty"
    val Tags = "tags"
    val Rate = "rate"
  }

  def questionsRepo = new QuestionReposMongoImp(reactiveMongoApi)

  val r = scala.util.Random

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
    val tags =  (request.body \ Tags).as[String]
    val rate = (request.body \ Rate).as[Int]

    questionsRepo.save(BSONDocument(
      Question -> question,
      Answer -> answer,
      DetailedAnswer -> detailedAnswer,
      Difficulty -> difficulty,
      Tags -> tags,
      Rate -> rate
    )).map(result => Created)
  }

  def update(id: String) = Action.async(BodyParsers.parse.json) { implicit  request =>

    val question = (request.body \ Question).as[String]
    val answer = (request.body \ Answer).as[String]
    val detailedAnswer = (request.body \ DetailedAnswer).as[String]
    val difficulty = (request.body \ Difficulty).as[String]
    val tags =  (request.body \ Tags).as[String]
    val rate = (request.body \ Rate).as[Int]

    questionsRepo.update(BSONDocument(Id -> BSONObjectID(id)), BSONDocument("$set" -> BSONDocument(
      Question -> question,
      Answer -> answer,
      DetailedAnswer -> detailedAnswer,
      Difficulty -> difficulty,
      Tags -> tags,
      Rate -> rate
    ))).map(result => Accepted)
  }

  def get_random_question() = Action.async { implicit  request =>
    questionsRepo.randomDocument().map(question => Ok(Json.toJson(question)))
  }

}
