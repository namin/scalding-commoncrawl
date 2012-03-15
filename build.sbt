import AssemblyKeys._

name := "scalding-commoncrawl"

version := "0.1"

organization := "net.namin"

scalaVersion := "2.9.1"

libraryDependencies += "com.twitter" %% "scalding" % "0.3.5"

parallelExecution in Test := false

seq(assemblySettings: _*)

mainClass in assembly := Some("com.twitter.scalding.Tool")

// Janino includes a broken signature, and is not needed.
// Also don't include Hadoop core, to run on EMR.
excludedJars in assembly <<= (fullClasspath in assembly) map { cp =>
  cp filter { Set("janino-2.5.16.jar", "hadoop-core-0.20.2.jar") contains _.data.getName}
}
