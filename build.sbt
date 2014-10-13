organization := "com.github.seratch"

name := "hackernews4s"

version := "0.1"

scalaVersion := "2.11.2"

crossScalaVersions := Seq("2.10.4", "2.11.2")

lazy val skinnyVersion = "1.3.3"

libraryDependencies := Seq(
  "org.skinny-framework" %% "skinny-http-client" % skinnyVersion,
  "org.skinny-framework" %% "skinny-json"        % skinnyVersion,
  "ch.qos.logback"       %  "logback-classic"    % "1.1.2" % "test",
  "org.scalatest"        %% "scalatest"          % "2.2.2" % "test"
)

parallelExecution in Test := false

scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature")

scalariformSettings

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
  
