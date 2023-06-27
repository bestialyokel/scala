ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.12.1"

lazy val root = (project in file("."))
  .settings(
    name := "scala-test"
  )
