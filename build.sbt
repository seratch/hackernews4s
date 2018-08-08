lazy val skinnyVersion = "3.0.0"

lazy val root = (project in file(".")).settings(
  organization := "com.github.seratch",
  name := "hackernews4s",
  version := "0.7.0",
  scalaVersion := "2.12.6",
  crossScalaVersions := Seq("2.12.6", "2.11.12"),
  libraryDependencies ++= Seq(
    "org.skinny-framework" %% "skinny-http-client" % skinnyVersion,
    "org.skinny-framework" %% "skinny-json"        % skinnyVersion,
    "ch.qos.logback"       %  "logback-classic"    % "1.2.+"  % "test",
    "org.scalatest"        %% "scalatest"          % "3.0.+"  % "test",
    "org.scalacheck"       %% "scalacheck"         % "1.13.+" % "test"
  ),
  scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature"),
  initialCommands := """import hackernews4s.v0._""",
  parallelExecution in Test := false,
  logBuffered in Test := false,
  doctestTestFramework := DoctestTestFramework.ScalaTest,
  publishMavenStyle := true,
  pomIncludeRepository := { x => false },
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
)
