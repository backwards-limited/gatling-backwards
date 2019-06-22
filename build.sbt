import BuildProperties._
import Dependencies._
import sbt._

lazy val root = project("gatling-backwards", file("."))
  .settings(description := "Backwards Gatling module aggregation - Gatling functionality includes example usage in various courses")
  .aggregate(beginnersCourse)

lazy val beginnersCourse = project("beginners", file("courses/beginners"))
  .settings(description := "Beginners Course")
  .settings(javaOptions in Test ++= Seq("-Dconfig.resource=application.test.conf"))
  .settings(javaOptions in IntegrationTest ++= Seq("-Dconfig.resource=application.it.conf"))

// TODO - Somehow reuse from module "scala-backwards"
def project(id: String, base: File): Project =
  Project(id, base)
    .enablePlugins(GatlingPlugin, JavaAppPackaging, DockerComposePlugin)
    .configs(IntegrationTest)
    .settings(Defaults.itSettings)
    .settings(testFrameworks in IntegrationTest := Seq(TestFrameworks.ScalaTest))
    .settings(
      resolvers ++= Seq(
        Resolver.sonatypeRepo("releases"),
        Resolver.bintrayRepo("cakesolutions", "maven"),
        "jitpack" at "https://jitpack.io",
        "Confluent Platform Maven" at "http://packages.confluent.io/maven/"
      ),
      scalaVersion := BuildProperties("scala.version"),
      sbtVersion := BuildProperties("sbt.version"),
      organization := "com.backwards",
      name := id,
      autoStartServer := false,
      triggeredMessage := Watched.clearWhenTriggered,
      addCompilerPlugin("org.typelevel" %% "kind-projector" % "0.10.3"),
      addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.1" cross CrossVersion.full),
      libraryDependencies ++= dependencies,
      excludeDependencies ++= Seq("org.slf4j" % "slf4j-log4j12"),
      fork := true,
      javaOptions in IntegrationTest ++= environment.map { case (key, value) => s"-D$key=$value" }.toSeq,
      scalacOptions ++= Seq("-Ypartial-unification"),
      publishArtifact in (Test, packageBin) := true,
      publishArtifact in (Test, packageSrc) := true,
      publishArtifact in (IntegrationTest, packageBin) := true,
      publishArtifact in (IntegrationTest, packageSrc) := true,
      assemblyJarName in assembly := s"$id.jar",
      test in assembly := {},
      assemblyMergeStrategy in assembly := {
        case PathList("javax", xs @ _*) => MergeStrategy.first
        case PathList("org", xs @ _*) => MergeStrategy.first
        case PathList(ps @ _*) if ps.last endsWith ".html" => MergeStrategy.first
        case PathList(ps @ _*) if ps.last endsWith "module-info.class" => MergeStrategy.first
        case "application.conf" => MergeStrategy.concat
        case x =>
          val oldStrategy = (assemblyMergeStrategy in assembly).value
          oldStrategy(x)
      }
    )
    .settings(
      // To use 'dockerComposeTest' to run tests in the 'IntegrationTest' scope instead of the default 'Test' scope:
      // 1) Package the tests that exist in the IntegrationTest scope
      testCasesPackageTask := (sbt.Keys.packageBin in IntegrationTest).value,
      // 2) Specify the path to the IntegrationTest jar produced in Step 1
      testCasesJar := artifactPath.in(IntegrationTest, packageBin).value.getAbsolutePath,
      // 3) Include any IntegrationTest scoped resources on the classpath if they are used in the tests
      testDependenciesClasspath := {
        val fullClasspathCompile = (fullClasspath in Compile).value
        val classpathTestManaged = (managedClasspath in IntegrationTest).value
        val classpathTestUnmanaged = (unmanagedClasspath in IntegrationTest).value
        val testResources = (resources in IntegrationTest).value
        (fullClasspathCompile.files ++ classpathTestManaged.files ++ classpathTestUnmanaged.files ++ testResources).map(_.getAbsoluteFile).mkString(java.io.File.pathSeparator)
      },
      packageName in Docker := s"${organization.value}/${name.value}",
      dockerImageCreationTask := (publishLocal in Docker).value
    )