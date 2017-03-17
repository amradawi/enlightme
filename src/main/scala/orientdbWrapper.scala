import com.orientechnologies.orient.core.metadata.schema.OType
import com.orientechnologies.orient.core.sql.OCommandSQL
import com.tinkerpop.blueprints.Vertex
import com.tinkerpop.blueprints.impls.orient._


val uri: String = "PLOCAL:/Users/amradawi/Desktop/orientdb/databases/enlightme_test"

class OridentDBWrapper{
  def createNewUser(firstName:String, lastName:String, username:String): DBStatus.status ={
    val factory: OrientGraphFactory = new OrientGraphFactory(uri)
    val graph: OrientGraph = factory.getTx()

    if (graph.getVertexType("User") != null) {
      val user: Vertex = graph.addVertex("class:User", Nil: _*)
      user.setProperty("firstName", firstName)
      user.setProperty("lastName", lastName)
      user.setProperty("username", username)
      DBStatus.OK
    }else{
      DBStatus.ERROR
    }
  }

  def createNewQuestion(): DBStatus.status={
    return DBStatus.OK
  }

  def createNewTag(): DBStatus.status={
    return DBStatus.OK
  }

  def readQuery(query:String): DBStatus.status={
    val factory: OrientGraphFactory = new OrientGraphFactory(uri)
    val graph: OrientGraph = factory.getTx()

    val res: OrientDynaElementIterable = graph
      .command(new OCommandSQL(query))
      .execute()
    return DBStatus.OK
  }

  def addEdge(): DBStatus.status={

    val factory: OrientGraphFactory = new OrientGraphFactory(uri)
    val graph: OrientGraph = factory.getTx()

    val res: OrientDynaElementIterable = graph
      .command(new OCommandSQL(s"SELECT expand(in('Work')) FROM Company WHERE name='ACME'"))
      .execute()
  }
}


object DBStatus extends Enumeration {
  type status = Value
  val OK, ERROR = Value
}
