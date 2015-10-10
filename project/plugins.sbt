addSbtPlugin("com.typesafe.sbt"     % "sbt-scalariform"      % "1.3.0")
addSbtPlugin("net.virtual-void"     % "sbt-dependency-graph" % "0.7.5")
addSbtPlugin("com.timushev.sbt"     % "sbt-updates"          % "0.1.9")
addSbtPlugin("com.jsuereth"         % "sbt-pgp"              % "1.0.0")
addSbtPlugin("com.github.mpeltonen" % "sbt-idea"             % "1.6.0")
addSbtPlugin("com.github.tkawachi"  % "sbt-doctest"          % "0.3.4")
addSbtPlugin("org.xerial.sbt"       % "sbt-sonatype"         % "0.5.0")

scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature")
