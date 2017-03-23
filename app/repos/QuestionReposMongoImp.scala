package repos

import play.api.libs.json.{JsObject, Json}
import play.modules.reactivemongo.ReactiveMongoApi
import play.modules.reactivemongo.json._
import play.modules.reactivemongo.json.collection.JSONCollection
import reactivemongo.api.ReadPreference
import reactivemongo.api.collections.GenericQueryBuilder
import reactivemongo.api.commands.WriteResult
import reactivemongo.bson.{BSONDocument, BSONObjectID}

import scala.concurrent.{ExecutionContext, Future}
/**
  * Created by amradawi on 2017-03-18.
  */
class QuestionReposMongoImp (reactiveMongoApi: ReactiveMongoApi) extends QuestionRepos[BSONDocument, WriteResult]{

  def collection = reactiveMongoApi.db.collection[JSONCollection]("questions")
  def AggFramework = collection.BatchCommands.AggregationFramework

  override def find()(implicit ec: ExecutionContext): Future[List[JsObject]] = {
    val genericQueryBuilder = collection.find(Json.obj())
    val cursor = genericQueryBuilder.cursor[JsObject](ReadPreference.Primary)
    cursor.collect[List]()
  }

  def find(query: BSONDocument)(implicit ec: ExecutionContext): Future[List[JsObject]] = {
    val genericQueryBuilder = collection.find(query)
    val cursor = genericQueryBuilder.cursor[JsObject](ReadPreference.Primary)
    cursor.collect[List]()
  }
  override def select(selector: BSONDocument)(implicit ec: ExecutionContext): Future[Option[JsObject]]= {
    collection.find(selector).one[JsObject]
  }

  override def update(selector: BSONDocument, update: BSONDocument)(implicit ec: ExecutionContext): Future[WriteResult] = {
    collection.update(selector, update)
  }

  override def remove(document: BSONDocument)(implicit ec: ExecutionContext): Future[WriteResult] = {
    collection.remove(document)
  }

  override def save(document: BSONDocument)(implicit ec: ExecutionContext): Future[WriteResult] = {
    collection.save(document)
  }

  def randomDocument()(implicit  ec: ExecutionContext): Future[List[BSONDocument]] = {
    collection.aggregate(AggFramework.Sample(1)).map(_.head[BSONDocument])
  }
}
