val circeVersion = "0.9.1"

lazy val root = (project in file("."))
  .settings(
    name := "HistogramModel",
    version := "0.1",
    scalaVersion := "2.11.11",
    assemblyJarName := "HistogramModel.jar",
    assemblyShadeRules in assembly := Seq(
      ShadeRule.rename("shapeless.**" -> "shadeshapless.@1").inAll
    ),
    libraryDependencies += "org.scalactic" %% "scalactic" % "3.0.4",
    libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.4" % "test",
    libraryDependencies += "org.json4s" %% "json4s-native" % "3.5.3"
  )

resolvers += Resolver.sonatypeRepo("releases")
addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full)