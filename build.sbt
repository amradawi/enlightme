name := "EnlightMe"

version := "0.4"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.8"

libraryDependencies += filters
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "1.5.1" % Test

libraryDependencies += "mysql" % "mysql-connector-java" % "5.1.6"

libraryDependencies += "javax.persistence" % "persistence-api" % "1.0-rev-1"

// https://mvnrepository.com/artifact/com.orientechnologies/orient-commons
libraryDependencies += "com.orientechnologies" % "orient-commons" % "2.0-M1"

// https://mvnrepository.com/artifact/com.orientechnologies/orientdb-core
libraryDependencies += "com.orientechnologies" % "orientdb-core" % "2.2.17"

//libraryDependencies += "org.scalatest" % "scalatest_2.9.0" % "1.6.1"

// https://mvnrepository.com/artifact/com.orientechnologies/orientdb-object
libraryDependencies += "com.orientechnologies" % "orientdb-object" % "2.2.17"

libraryDependencies ++= Seq(
  "com.orientechnologies" % "orientdb-graphdb" % "2.2.3"
)
libraryDependencies ++= Seq(
  jdbc,
  cache,
  ws,
  specs2 % Test
)

libraryDependencies ++= Seq(
  "org.reactivemongo" %% "play2-reactivemongo" % "0.11.14"
)