name := """carona-ufcg"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)
lazy val myProject = (project in file(".")).enablePlugins(PlayJava, PlayEbean)

scalaVersion := "2.11.6"

libraryDependencies ++= Seq(
  javaJdbc,
  cache,
  javaWs
)
dependencyOverrides +=  "org.apache.httpcomponents" % "httpclient" % "4.3.4";
dependencyOverrides +=  "com.google.guava" % "guava" % "18.0";
dependencyOverrides +=  "junit" % "junit" % "4.12";
dependencyOverrides +=  "org.apache.httpcomponents" % "httpcore" % "4.3.2";
dependencyOverrides +=  "commons-logging" % "commons-logging" % "1.1.3";
dependencyOverrides +=  "org.scala-lang.modules" % "scala-parser-combinators_2.11" % "1.0.3";
dependencyOverrides +=  "org.scala-lang.modules" % "scala-xml_2.11" % "1.0.3";

// Play provides two styles of routers, one expects its actions to be injected, the
// other, legacy style, accesses its actions statically.
routesGenerator := InjectedRoutesGenerator
