package com.backwards.gatling

import scala.concurrent.duration._
import scala.language.postfixOps
import io.gatling.core.Predef._
import io.gatling.core.feeder.SourceFeederBuilder
import io.gatling.core.structure.ScenarioBuilder
import io.gatling.http.Predef._
import io.gatling.http.protocol.HttpProtocolBuilder

class Simulation3 extends Simulation with Constants {
  val authToken = "authenticity_token"

  val httpProtocol: HttpProtocolBuilder =
    http.baseUrl("https://cheeze-flight-booker.herokuapp.com")
      .inferHtmlResources()
      .userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_0) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3578.98 Safari/537.36")
      .silentResources

  val headers_0: Map[String, String] = Map(
    "Accept" -> "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8",
    "Accept-Encoding" -> "gzip, deflate, br",
    "Accept-Language" -> "en-US,en;q=0.9",
    "Upgrade-Insecure-Requests" -> "1")

  val headers_1: Map[String, String] = Map(
    "Accept" -> "*/*",
    "Accept-Encoding" -> "gzip, deflate, br",
    "Accept-Language" -> "en-US,en;q=0.9")

  val headers_2: Map[String, String] = Map(
    "Accept" -> "image/webp,image/apng,image/*,*/*;q=0.8",
    "Accept-Encoding" -> "gzip, deflate, br",
    "Accept-Language" -> "en-US,en;q=0.9",
    "Pragma" -> "no-cache")

  val headers_11: Map[String, String] = Map(
    "Accept" -> "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8",
    "Accept-Encoding" -> "gzip, deflate, br",
    "Accept-Language" -> "en-US,en;q=0.9",
    "Origin" -> "https://cheeze-flight-booker.herokuapp.com",
    "Upgrade-Insecure-Requests" -> "1"
  )

  val csvFeeder: SourceFeederBuilder[String] = csv("data/passengers.csv").queue // Not required as ".queue" is the default

  // If the feeder is large, rather than loading it entirely into memory, we can batch e.g.
  // val csvFeeder: SourceFeederBuilder[String] = csv("data/passengers.csv").batch

  // Example of a custom feeder where each element of row/line is delimited by a '#'
  // val customSeparatorFeeder: SourceFeederBuilder[String] = separatedValues("data/my-feeder.txt", '#')

  val scn: ScenarioBuilder = scenario("SE")
    .exec(flushHttpCache)       // Clear cache
    .exec(flushSessionCookies)  // Clear session cookies
    .exec(
      http("Homepage")
        .get("/")
        .headers(headers_0)
        .resources(
          http("request_1")
            .get("/assets/application-2534172286055efef05dbb34d2da8fc2.js")
            .headers(headers_1)
        )
        .check(status.in(200, 201, 202, 304))
        .check(status.not(404))
    )
    .pause(1 second)

    .exec(
      http("request_2")
        .get("/favicon.ico")
        .headers(headers_2)
        .silent
    )
    .pause(10 seconds, 14 seconds) // i.e. pause between 10 and 14 seconds (selected randomly)

    .exec(
      http("SearchFlight")
        .get("/flights?utf8=%E2%9C%93&from=1&to=2&date=2015-01-03&num_passengers=2&commit=search")
        .headers(headers_0)
        .resources(
          http("request_4")
            .get("/assets/application-2534172286055efef05dbb34d2da8fc2.js")
            .headers(headers_1),
          http("request_5")
            .get("/assets/application-c99cbb3caf78d16bb1482ca2e41d7a9c.css")
        )
        .check(currentLocationRegex(".*num_passengers=2.*"))
    )
    .pause(1 second)

    .exec(
      http("request_6")
        .get("/favicon.ico")
        .silent
    )
    .pause(10 seconds)

    .exec(
      http("SelectFlight")
        .get("/bookings/new?utf8=%E2%9C%93&flight_id=10&commit=Select+Flight&num_passengers=2")
        .headers(headers_0)
        .resources(
          http("request_8")
            .get("/assets/application-2534172286055efef05dbb34d2da8fc2.js")
            .headers(headers_1),
          http("request_9")
            .get("/assets/application-c99cbb3caf78d16bb1482ca2e41d7a9c.css")
        )
        .check(css("h1:contains('Book Flight')").exists)
        .check(substring("Email").find.exists)
        .check(substring("Email").count.is(2))
        .check(css(s"""input[name="$authToken"]""", value) saveAs authToken)
        .check(bodyString saveAs body)
    )
    .exec { session =>
      println("===> Auth token = " + session(authToken).as[String])
      println("===> Body = " + session(body).as[String])
      session
    }
    .pause(1 second)

    .exec(
      http("request_10")
        .get("/favicon.ico")
        .silent
    )
    .pause(10 seconds)

    .feed(csvFeeder, 2) // This will essentially take 2 lines (1 being the default) at a time to feed said lines into the following "exec"
    .exec(
      http("BookFlight")
        .post("/bookings")
        .headers(headers_11)
        .formParam("utf8", "✓")
        .formParam(authToken, s"$${$authToken}")
        .formParam("booking[flight_id]", "10")
        .formParam("booking[passengers_attributes][0][name]", "${name1}")    // Where ${name1} is taken from the provided feeder data.
        .formParam("booking[passengers_attributes][0][email]", "${email1}")  // Where ${email1} is taken from the provided feeder data.
        .formParam("booking[passengers_attributes][1][name]", "${name2}")
        .formParam("booking[passengers_attributes][1][email]", "${email2}")
        .formParam("commit", "Book Flight")
    )
    .pause(1 second)

    .exec(
      http("request_12")
        .get("/favicon.ico")
        .silent
    )

  setUp(
    scn.inject(atOnceUsers(1))
  )
  .protocols(httpProtocol)
  .assertions(
    global.responseTime.max.lt(1000),
    forAll.responseTime.max.lt(1000),
    details("BookFlight").responseTime.max.lt(1000),
    global.successfulRequests.percent.is(100)
  )
}