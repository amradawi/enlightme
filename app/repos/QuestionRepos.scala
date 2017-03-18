package repos

import play.api.libs.json.JsObject
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
