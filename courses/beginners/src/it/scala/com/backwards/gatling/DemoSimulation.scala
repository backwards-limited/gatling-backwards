package com.backwards.gatling

import io.gatling.core.Predef._
import io.gatling.core.structure.ScenarioBuilder
import io.gatling.http.Predef._
import io.gatling.http.protocol.HttpProtocolBuilder

class DemoSimulation extends Simulation {
  val httpProtocol: HttpProtocolBuilder = http
    .baseUrl("http://computer-database.gatling.io") // baseUrl that will be prepended to all relative urls
    .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8") // Common HTTP headers that will be sent with all the requests
    .doNotTrackHeader("1")
    .acceptLanguageHeader("en-US,en;q=0.5")
    .acceptEncodingHeader("gzip, deflate")
    .userAgentHeader("Mozilla/5.0 (Windows NT 5.1; rv:31.0) Gecko/20100101 Firefox/31.0")

  val scn: ScenarioBuilder = scenario("DemoSimulation") // The scenario definition
    .exec(http("request_1") // A HTTP request, named request_1 - This name will be displayed in the final reports
    .get("/")) // The url this request targets with the GET method
    .pause(5) // Some pause/think time

  setUp( // Where one sets up the scenarios that will be launched in this Simulation
    scn.inject(atOnceUsers(1)) // Declaring to inject into scenario named scn one single user
  ).protocols(httpProtocol) // Attaching the HTTP configuration declared above
}