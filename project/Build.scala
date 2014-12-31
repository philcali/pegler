import sbt._
import Keys._

object Build extends sbt.Build {
  override lazy val settings = super.settings ++ Seq(
    organization := "com.github.philcali",
    scalaVersion := "2.11.4",
    version := "0.0.1",
    libraryDependencies += "org.scalatest" %% "scalatest" % "2.2.1" % "test"
  )

  lazy val root = Project(
    "pegler",
    file(".")
  ) aggregate (core, argonaut, dynamodb, service, app)

  lazy val core = Project(
    id = "pegler-core",
    base = file("core")
  )

  lazy val dynamodb = Project(
    id = "pegler-dynamodb",
    base = file("dynamodb"),
    settings = Project.defaultSettings ++ Seq(
      libraryDependencies += "com.amazonaws" % "aws-java-sdk-dynamodb" % "1.9.13"
    )
  ) dependsOn core

  lazy val argonaut = Project(
    id = "pegler-argonaut",
    base = file("argonaut"),
    settings = Project.defaultSettings ++ Seq(
      libraryDependencies += "io.argonaut" %% "argonaut" % "6.0.4"
    )
  ) dependsOn core

  lazy val service = Project(
    id = "pegler-service",
    base = file("service"),
    settings = Project.defaultSettings ++ Seq(
      libraryDependencies += "net.databinder" %% "unfiltered-directives" % "0.8.3"
    )
  ) dependsOn argonaut

  lazy val app = Project(
    id = "pegler-app",
    base = file("app"),
    settings = Project.defaultSettings ++ Seq(
      libraryDependencies ++= Seq(
        "net.databinder" %% "unfiltered-jetty" % "0.8.3",
        "net.databinder" %% "unfiltered-filter-uploads" % "0.8.3"
      )
    )
  ) dependsOn (service, dynamodb)
}
