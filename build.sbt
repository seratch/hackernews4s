lazy val skinnyVersion = "1.3.+"

organization := "com.github.seratch"
name := "hackernews4s"
version := "0.4.1"
scalaVersion := "2.11.6"
crossScalaVersions := Seq("2.10.5", "2.11.6")
libraryDependencies := Seq(
  "org.skinny-framework" %% "skinny-http-client" % skinnyVersion,
  "org.skinny-framework" %% "skinny-json"        % skinnyVersion,
  "ch.qos.logback"       %  "logback-classic"    % "1.1.+"  % "test",
  "org.scalatest"        %% "scalatest"          % "2.2.+"  % "test",
  "org.scalacheck"       %% "scalacheck"         % "1.12.+" % "test"
)
scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature")
initialCommands := """import hackernews4s.v0._"""
parallelExecution in Test := false
logBuffered in Test := false
scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature")

// ------
// scalariform
scalariformSettings

// ------
// sbt-doctest
doctestSettings
doctestWithDependencies := false
doctestTestFramework := DoctestTestFramework.ScalaTest

// ------
// publish settings
sonatypeSettings
publishTo <<= version { (v: String) => 
  val nexus = "https://oss.sonatype.org/"
  if (v.trim.endsWith("SNAPSHOT")) Some("snapshots" at nexus + "content/repositories/snapshots")
  else Some("releases" at nexus + "service/local/staging/deploy/maven2")
}
publishMavenStyle := true
pomIncludeRepository := { x => false }
pomExtra := <url>https://github.com/serach/hackernews4s/</url>
  <licenses>
    <license>
      <name>MIT License</name>
      <url>http://www.opensource.org/licenses/mit-license.php</url>
      <distribution>repo</distribution>
    </license>
  </licenses>
  <scm>
    <url>git@github.com:seratch/hackernews4s.git</url>
    <connection>scm:git:git@github.com:seratch/hackernews4s.git</connection>
  </scm>
  <developers>
    <developer>
      <id>seratch</id>
      <name>Kazuhiro Sera</name>
      <url>http://git.io/sera</url>
    </developer>
  </developers>
  
