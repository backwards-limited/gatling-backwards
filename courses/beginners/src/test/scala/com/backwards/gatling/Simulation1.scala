package com.backwards.gatling

import io.gatling.core.Predef._
import io.gatling.core.structure.ScenarioBuilder
import io.gatling.http.Predef._
import io.gatling.http.protocol.HttpProtocolBuilder

/**
  * Scenario:
  * - Go to site http://newtours.demoaut.com
  * - From left navigation pane, click on "Cruises"
  */
class Simulation1 extends Simulation {
  val httpProtocol: HttpProtocolBuilder =
    http.baseUrl("http://newtours.demoaut.com")
      .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8")
      .doNotTrackHeader("1")
      .acceptLanguageHeader("en-US,en;q=0.5")
      .acceptEncodingHeader("gzip, deflate br")
      .userAgentHeader("Mozilla/5.0 (Windows NT 5.1; rv:31.0) Gecko/20100101 Firefox/31.0")

  val scn: ScenarioBuilder =
    scenario("ViewCruise").exec(http("request-1") get "/mercurycruise.php").pause(10)

  setUp(
    scn.inject(atOnceUsers(2))
  ).protocols(httpProtocol)
}