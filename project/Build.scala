import sbt._
import Keys._

object Build extends sbt.Build {
  override lazy val settings = super.settings ++ Seq(
    organization := "com.github.philcali",
    version := "0.0.1"
  )

  lazy val root = Project(
    "pegler",
    file(".")
  ) aggregate core

  lazy val core = Project(
    id = "pegler-core",
    base = file("core"),
    settings = Project.defaultSettings ++ Seq(
      libraryDependencies += "org.scalatest" %% "scalatest" % "2.2.1" % "test"
    )
  )
}
