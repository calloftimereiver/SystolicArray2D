ThisBuild / version := "1.0"
ThisBuild / scalaVersion := "2.12.18"
ThisBuild / organization := "org.example"

val spinalVersion = "1.10.1"
val spinalCore = "com.github.spinalhdl" %% "spinalhdl-core" % spinalVersion
val spinalLib = "com.github.spinalhdl" %% "spinalhdl-lib" % spinalVersion
val spinalIdslPlugin = compilerPlugin("com.github.spinalhdl" %% "spinalhdl-idsl-plugin" % spinalVersion)

lazy val projectname = (project in file("."))
  .settings(
    Compile / scalaSource := baseDirectory.value / "hw" / "spinal",
    libraryDependencies  ++= Seq(
    spinalCore, spinalLib, spinalIdslPlugin,
    "org.scalanlp" %% "breeze" % "1.1",
    "org.scalanlp" %% "breeze-natives" % "1.1",
    "org.scalanlp" %% "breeze-viz" % "1.1"
    )
  )

fork := true


