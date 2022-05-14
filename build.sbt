lazy val akkaHttpVersion = "10.2.4"
lazy val akkaVersion     = "2.6.15"

lazy val root = (project in file("."))
  .enablePlugins(DockerPlugin)
  .enablePlugins(AshScriptPlugin)
  .settings(
    inThisBuild(
      List(
        organization := "in.hcl",
        scalaVersion := "2.13.4",
        version      := "1.0"
      )
    ),
    name := "HCL_Trades",
    libraryDependencies ++= Seq(
      "com.typesafe" % "config" % "1.4.0",
      //"org.typelevel" %% "cats-effect" % "2.6.1",
      "com.typesafe.akka"             %% "akka-http"                  % akkaHttpVersion,
      "com.typesafe.akka"             %% "akka-http-spray-json"       % akkaHttpVersion,
      "com.typesafe.akka"             %% "akka-actor-typed"           % akkaVersion,
      "com.typesafe.akka"             %% "akka-stream"                % akkaVersion,
      "com.typesafe.akka"             %% "akka-cluster-typed"         % akkaVersion,
      "ch.qos.logback"                 % "logback-classic"            % "1.2.3",
      "com.softwaremill.sttp.tapir"   %% "tapir-akka-http-server"     % "0.18.0-M18",
      "com.softwaremill.sttp.client3" %% "core"                       % "3.3.9",
      "com.softwaremill.sttp.tapir"   %% "tapir-json-circe"           % "0.18.0-M18",
      "io.circe"                      %% "circe-optics"               % "0.14.1",
      "com.softwaremill.sttp.tapir"   %% "tapir-openapi-docs"         % "0.18.0-M18",
      "com.softwaremill.sttp.tapir"   %% "tapir-openapi-circe-yaml"   % "0.18.0-M18",
      "com.softwaremill.sttp.tapir"   %% "tapir-swagger-ui-akka-http" % "0.18.0-M18",
      "com.softwaremill.sttp.tapir"   %% "tapir-redoc-akka-http"      % "0.18.0-M18",
      "com.typesafe.scala-logging"    %% "scala-logging"              % "3.9.3",
      "org.postgresql"                 % "postgresql"                 % "9.3-1102-jdbc41",
      "joda-time"                      % "joda-time"                  % "2.10.10",
      "org.tpolecat"                  %% "doobie-core"                % "0.12.1",
      "org.tpolecat"                  %% "doobie-postgres"            % "0.12.1",
      "org.tpolecat"                  %% "doobie-postgres-circe"      % "0.12.1",
      "com.typesafe.akka"             %% "akka-http-testkit"          % akkaHttpVersion % Test,
      "com.typesafe.akka"             %% "akka-actor-testkit-typed"   % akkaVersion     % Test,
      "org.scalatest"                 %% "scalatest"                  % "3.1.4"         % Test
    ),
    Docker / packageName := "trades",
    dockerExposedPorts ++= Seq(6067),
    dockerRepository   := Some("hcl"),
    dockerBaseImage    := "openjdk:11-jre-slim-buster",
    dockerUpdateLatest := true
  )
