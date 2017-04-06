import Dependencies._


lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      organization := "io.github.alexbergeron",
      scalaVersion := "2.12.1",
      scalaOrganization in ThisBuild := "org.typelevel",
      version      := "0.1.0-SNAPSHOT"
    )),
    name := "hello-matryoshka",
    libraryDependencies ++= Seq(
      matryoshka
    )
  )
