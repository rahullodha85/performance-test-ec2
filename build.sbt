enablePlugins(GatlingPlugin)

//fork in run := true

scalaVersion := "2.11.7"

scalacOptions := Seq(
  "-encoding", "UTF-8", "-target:jvm-1.7", "-deprecation",
  "-feature", "-unchecked", "-language:implicitConversions", "-language:postfixOps")

//javaOptions in Gatling := Seq("-Xms2G", "-Xmx5G")
javaOptions in Gatling += "-Xms2G"
javaOptions in Gatling += "-Xmx5G"

libraryDependencies += "io.gatling.highcharts" % "gatling-charts-highcharts" % "2.1.7" % "test"
libraryDependencies += "io.gatling"            % "gatling-test-framework"    % "2.1.7" % "test"
libraryDependencies += "org.scalactic" %% "scalactic" % "2.2.6"
libraryDependencies += "org.scalatest" %% "scalatest" % "2.2.6" % "test"
libraryDependencies += "org.scalaj" % "scalaj-http_2.11" % "2.3.0"
libraryDependencies += "net.liftweb" %% "lift-json" % "2.6"
libraryDependencies += "com.jayway.restassured" % "scala-support" % "2.9.0"
libraryDependencies += "net.debasishg" %% "redisclient" % "3.0"
libraryDependencies += "com.typesafe.akka" %% "akka-actor" % "2.4.12"
libraryDependencies += "net.databinder.dispatch" % "dispatch-core_2.10" % "0.11.3"
libraryDependencies += "net.databinder" % "dispatch-http_2.9.1" % "0.8.10"
libraryDependencies += "com.google.inject" % "guice" % "3.0"
libraryDependencies += "org.mongodb.scala" %% "mongo-scala-driver" % "1.2.1"
libraryDependencies += "org.mongodb" %% "casbah" % "3.1.1"
libraryDependencies += "com.typesafe.slick" %% "slick" % "3.1.1"
libraryDependencies += "com.typesafe.slick" %% "slick-extensions" % "3.1.0"

resolvers += "Typesafe Releases" at "http://repo.typesafe.com/typesafe/maven-releases/"

resolvers += "Akka Snapshot Repository" at "http://repo.akka.io/snapshots/"

unmanagedJars in Compile <<= baseDirectory map { base =>
  val libs = base / "libraries"
  val dirs = (libs / "jars")
  (dirs ** "*.jar").classpath }