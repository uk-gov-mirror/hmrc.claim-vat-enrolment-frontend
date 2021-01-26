import play.core.PlayVersion.current
import play.sbt.PlayImport._
import sbt.Keys.libraryDependencies
import sbt._

object AppDependencies {

  val compile = Seq(
    "uk.gov.hmrc"             %% "bootstrap-frontend-play-27" % "3.3.0",
    "uk.gov.hmrc"             %% "play-frontend-hmrc"         % "0.38.0-play-27",
    "uk.gov.hmrc"             %% "play-frontend-govuk"        % "0.60.0-play-27",
    "uk.gov.hmrc"             %% "play-language"              % "4.10.0-play-27"
  )

  val test = Seq(
    "uk.gov.hmrc"             %% "bootstrap-test-play-27"     % "3.3.0" % Test,
    "org.scalatest"           %% "scalatest"                  % "3.2.3"  % Test,
    "org.jsoup"               %  "jsoup"                      % "1.13.1" % Test,
    "com.typesafe.play"       %% "play-test"                  % current  % Test,
    "com.vladsch.flexmark"    %  "flexmark-all"               % "0.36.8" % "test, it",
    "org.scalatestplus.play"  %% "scalatestplus-play"         % "4.0.3"  % "test, it",
    "org.scoverage"           %% "scalac-scoverage-runtime"   % "1.4.1"   % "test, it"
  )
}
