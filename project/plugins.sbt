addSbtPlugin("org.scalariform"      % "sbt-scalariform"      % "1.8.2")
addSbtPlugin("com.timushev.sbt"     % "sbt-updates"          % "0.3.4")
addSbtPlugin("com.jsuereth"         % "sbt-pgp"              % "1.1.1")
addSbtPlugin("com.github.tkawachi"  % "sbt-doctest"          % "0.8.0")
addSbtPlugin("org.xerial.sbt"       % "sbt-sonatype"         % "2.3")

scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature")
