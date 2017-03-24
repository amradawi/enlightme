package controllers

import javax.inject._
import play.api.Logger
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json.Json
import play.api.mvc.{Action, Controller}
import play.modules.reactivemongo.json.collection.JSONCollection
import play.modules.reactivemongo.{MongoController, ReactiveMongoApi, ReactiveMongoComponents}
import reactivemongo.api.collections.bson.BSONCollection
import reactivemongo.api.commands.bson.BSONCountCommand.{ Count, CountResult }
import reactivemongo.api.commands.bson.BSONCountCommandImplicits._
import reactivemongo.bson.BSONDocument

import scala.concurrent.Future

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject() (val reactiveMongoApi: ReactiveMongoApi) extends Controller
  with MongoController with ReactiveMongoComponents {

  /**
   * Create an Action to render an HTML page.
   *
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */

  def jsonCollection = reactiveMongoApi.db.collection[JSONCollection]("questions");
  def bsonCollection = reactiveMongoApi.db.collection[BSONCollection]("questions");

  def index = Action { implicit request =>

//    val posts = List(
////      Json.obj(
////        "question" -> "What is the difference between stochastic gradient descent (SGD) and gradient descent (GD)?",
////        "short_answer" -> "Both algorithms are methods for finding a set of parameters that minimize a loss function by evaluating parameters against data and then making adjustments.\n\nIn standard gradient descent, you'll evaluate all training samples for each set of parameters. This is akin to taking big, slow steps toward the solution.\n\nIn stochastic gradient descent, you'll evaluate only 1 training sample for the set of parameters before updating them. This is akin to taking small, quick steps toward the solution.\n",
////        "long_answer" -> "",
////        "difficulty" -> "easy",
////        "tags" -> "Machine learning",
////        "rate" -> 5
////      ))
////      Json.obj(
//    //        "name" -> "Widget Two: The Return",
//    //        "description" -> "My second widget",
//    //        "author" -> "Justin"
//    //      ))
//
//    val query = BSONDocument("question" -> BSONDocument("$exists" -> true))
//    val command = Count(query)
//    val result: Future[CountResult] = bsonCollection.runCommand(command)
//
//    result.map { res =>
//      val numberOfDocs: Int = res.value
//      if(numberOfDocs < 1) {
//        jsonCollection.bulkInsert(posts.toStream, ordered = true).foreach(i => Logger.info("Record added."))
//      }
//    }
    Ok("Your new application is ready.")
  }

  def cleanup = Action {
    jsonCollection.drop(true).onComplete {
      case _ => Logger.info("Database collection dropped")
    }
    Ok("dtatbase has been dropped")
  }
}
