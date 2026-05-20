name := "scalajs-on-nodejs"

enablePlugins(ScalaJSPlugin)

version := "0.1"

scalaVersion := "3.3.4"

scalaJSUseMainModuleInitializer := true

libraryDependencies += "org.scala-js" %%% "scalajs-dom" % "2.8.0"

lazy val publishJs = taskKey[Unit]("Copy fastOptJS output into resources for the static server")

publishJs := {
  val linked = (Compile / fastOptJS).value
  val dest = (Compile / resourceDirectory).value / "main.js"
  IO.copyFile(linked.data, dest, preserveLastModified = true)
}

addCommandAlias("buildJs", "fastOptJS; publishJs")
