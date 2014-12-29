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
  ) aggregate (core, argonaut)

  lazy val core = Project(
    id = "pegler-core",
    base = file("core")
  )

  lazy val argonaut = Project(
    id = "pegler-argonaut",
    base = file("argonaut"),
    settings = Project.defaultSettings ++ Seq(
      libraryDependencies += "io.argonaut" %% "argonaut" % "6.0.4"
    )
  ) dependsOn core
}
