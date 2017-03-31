package Models

import play.api.data._
import play.api.data.Forms.{ text, longNumber, mapping, nonEmptyText, optional }

/**
  * Created by amradawi on 2017-03-25.
  */
case class Query (collection: String,
                  query: String)

object Query {
  val QueryForm = Form(mapping(
    "query" -> nonEmptyText,
    "collection" -> nonEmptyText
  ){(query, collection) =>
    Query(collection, query)
  }{
    query => Some(query.collection,
      query.query)
  }
  )
}
