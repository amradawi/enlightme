package schema
import com.orientechnologies.orient.core.metadata.schema.OType
import com.tinkerpop.blueprints.impls.orient.{OrientEdgeType, OrientGraph, OrientGraphFactory, OrientVertexType}
/**
  * OrientDb Schema builder
  * Created by amradawi on 2017-03-14.
  */
object SchemaBuilder {

  def createSchema(): Unit ={

    val uri: String = "PLOCAL:/Users/amradawi/Desktop/orientdb/databases/enlightme_test"
    val factory: OrientGraphFactory = new OrientGraphFactory(uri)
    val graph: OrientGraph = factory.getTx()

    val user: OrientVertexType = graph.createVertexType("User")
    user.createProperty("firstName", OType.STRING)
    user.createProperty("lastName", OType.STRING)
    user.createProperty("emailAddress", OType.STRING)

    val tag: OrientVertexType = graph.createVertexType("Tag")
    tag.createProperty("Name", OType.STRING)

    val question: OrientVertexType = graph.createVertexType("Question")
    question.createProperty("Answer", OType.STRING)
    question.createProperty("Source", OType.STRING)

    val seen: OrientEdgeType = graph.createEdgeType("SeenQuestion")
    seen.createProperty("seenQuestion", OType.INTEGER)

  }

  def main(args: Array[String]): Unit = {
    createSchema()
  }
}


