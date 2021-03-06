import sbtunidoc.Plugin._


organization in ThisBuild := "com.pellucid"

licenses in ThisBuild += ("Apache-2.0", url("http://www.apache.org/licenses/LICENSE-2.0"))

version in ThisBuild := "0.7-alpha-11"


scalaVersion in ThisBuild := "2.11.2"

crossScalaVersions in ThisBuild := Seq("2.10.4", "2.11.2")

scalacOptions in ThisBuild ++= Seq("-deprecation", "-feature", "-unchecked")


resolvers in ThisBuild ++= Seq(
    Resolver.sonatypeRepo("releases"),
    Resolver.typesafeRepo("releases"),
    "clojars" at "https://clojars.org/repo",
    "couchbase" at "http://files.couchbase.com/maven2"
  )


shellPrompt in ThisBuild := CustomShellPrompt.customPrompt


// configure publishing to bintray
bintray.Plugin.bintraySettings

bintray.Keys.bintrayOrganization in bintray.Keys.bintray := Some("pellucid")


lazy val datomisca = project.
  in(file(".")).
  aggregate(macros, core, tests, integrationTests)

// needed for aggregated build
MacroSettings.settings

libraryDependencies += Dependencies.Compile.datomic

// disable some aggregation tasks for subprojects
aggregate in doc            := false

aggregate in Keys.`package` := false

aggregate in packageBin     := false

aggregate in packageDoc     := false

aggregate in packageSrc     := false

aggregate in publish        := false

aggregate in publishLocal   := false


lazy val macros = project in file("macros")

// map macros project classes and sources into root project
mappings in (Compile, packageBin) <++= mappings in (macros, Compile, packageBin)

mappings in (Compile, packageSrc) <++= mappings in (macros, Compile, packageSrc)


lazy val core = project.
  in(file("core")).
  dependsOn(macros)

// map core project classes and sources into root project
mappings in (Compile, packageBin) <++= mappings in (core, Compile, packageBin)

mappings in (Compile, packageSrc) <++= mappings in (core, Compile, packageSrc)


lazy val tests = project.
  in(file("tests")).
  dependsOn(macros, core)


lazy val integrationTests = project.
  in(file("integration")).
  dependsOn(macros, core).
  configs(IntegrationTest)
