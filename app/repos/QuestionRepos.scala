package repos

import javax.inject.Inject

import play.api.libs.json.{JsObject, Json}
//import play.modules.reactivemongo.ReactiveMongoApi
//import play.modules.reactivemongo.json._
//import play.modules.reactivemongo.json.collection.JSONCollection
//import reactivemongo.api.ReadPreference
//import reactivemongo.api.commands.WriteResult
//import reactivemongo.bson.{BSONDocument, BSONObjectID}

import scala.concurrent.{ExecutionContext, Future}

/**
  * Created by amradawi on 2017-03-17.
  */
trait QuestionRepos[T, T1] {

  def find()(implicit ec: ExecutionContext): Future[List[JsObject]]

  def select(selector: T)(implicit ec: ExecutionContext): Future[Option[JsObject]]

  def update(selector: T, update: T)(implicit ec: ExecutionContext): Future[T1]

  def remove(document: T)(implicit ec: ExecutionContext): Future[T1]

  def save(document: T)(implicit ec: ExecutionContext): Future[T1]

}
