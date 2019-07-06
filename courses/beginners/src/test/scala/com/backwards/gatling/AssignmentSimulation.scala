package com.backwards.gatling

import scala.concurrent.duration._
import scala.language.postfixOps
import io.gatling.core.Predef._
import io.gatling.core.feeder.SourceFeederBuilder
import io.gatling.core.structure.ScenarioBuilder
import io.gatling.http.Predef._
import io.gatling.http.protocol.HttpProtocolBuilder

class AssignmentSimulation extends Simulation {
	val httpProtocol: HttpProtocolBuilder = http
		.baseUrl("http://newtours.demoaut.com")
		.inferHtmlResources()
  	.silentResources // Silence Resources

	val headers_0: Map[String, String] = Map(
		"Accept" -> "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8",
		"Accept-Encoding" -> "gzip, deflate",
		"Accept-Language" -> "en-US,en;q=0.9",
		"Upgrade-Insecure-Requests" -> "1",
		"User-Agent" -> "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_0) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3578.98 Safari/537.36"
	)

	val headers_11: Map[String, String] = Map(
		"Accept" -> "image/webp,image/apng,image/*,*/*;q=0.8",
		"Accept-Encoding" -> "gzip, deflate",
		"Accept-Language" -> "en-US,en;q=0.9",
		"User-Agent" -> "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_0) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3578.98 Safari/537.36"
	)

	val headers_21: Map[String, String] = Map(
		"Accept" -> "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8",
		"Accept-Encoding" -> "gzip, deflate",
		"Accept-Language" -> "en-US,en;q=0.9",
		"Origin" -> "http://newtours.demoaut.com",
		"Upgrade-Insecure-Requests" -> "1",
		"User-Agent" -> "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_0) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3578.98 Safari/537.36"
	)

	val headers_26: Map[String, String] = Map(
		"User-Agent" -> "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_0) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3578.98 Safari/537.36"
	)

	val csvFeeder: SourceFeederBuilder[String] =
		csv("data/assignment.csv").random	// CSV Feeder for Passengers' detail

	val scn: ScenarioBuilder = scenario("MercuryTours")
		.exec(flushHttpCache) // Delete Cache
		.exec(flushSessionCookies) // Delete Session Cookies

		.exec(http("Homepage") // Rename recorded Request
			.get("/mercurywelcome.php")
			.headers(headers_0)
			.resources(http("request_1")
			.get("/images/nav/logo.gif"),
            http("request_2")
			.get("/images/nav/html.gif"),
            http("request_3")
			.get("/images/nav/boxad1.gif"),
            http("request_4")
			.get("/images/banner2.gif"),
            http("request_5")
			.get("/images/featured_destination.gif"),
            http("request_6")
			.get("/images/hdr_specials.gif"),
            http("request_7")
			.get("/images/hdr_tips.gif"),
            http("request_8")
			.get("/images/tip93.gif"),
            http("request_9")
			.get("/images/hdr_findflight.gif"),
            http("request_10")
			.get("/images/hdr_right.gif"),
            http("request_11")
			.get("/images/btn_signin.gif")
			.headers(headers_11),
            http("request_12")
			.get("/images/hdr_destinations.gif"),
            http("request_13")
			.get("/images/icn_destinations.gif"),
            http("request_14")
			.get("/images/hdr_vacation.gif"),
            http("request_15")
			.get("/images/icn_vacations.gif"),
            http("request_16")
			.get("/images/hdr_register.gif"),
            http("request_17")
			.get("/images/icn_register.gif"),
            http("request_18")
			.get("/images/hdr_links.gif"),
            http("request_19")
			.get("/images/spacer.gif")
			.headers(headers_11)
			.check(status.is(404)),
            http("request_20")
			.get("/black")
			.headers(headers_11)
			.check(status.is(404)))
			.check(bodyString saveAs "responseData") // Save response data
			.check(substring("userName")) // Check 'userName' is present
		)

  	.exec {	// Print response data
			session =>
				println(session("responseData").as[String])
				session
		}

		.pause(10000 milliseconds) // Pause 10000 milliseconds

		.exec(http("Login") // Rename recorded Request
			.post("/login.php")
			.headers(headers_21)
			.formParam("action", "process")
			.formParam("userName", "tutorial")
			.formParam("password", "tutorial")
			.formParam("login.x", "30")
			.formParam("login.y", "7")
			.resources(http("request_22")
			.get("/images/nav/logo.gif"),
            http("request_23")
			.get("/images/nav/html.gif"),
            http("request_24")
			.get("/images/nav/boxad1.gif"),
            http("request_25")
			.get("/images/banner2.gif"),
            http("request_26")
			.get("/images/masts/mast_flightfinder.gif")
			.headers(headers_26),
            http("request_27")
			.get("/images/forms/continue.gif")
			.headers(headers_26),
            http("request_28")
			.get("/images/spacer.gif")
			.headers(headers_11)
			.check(status.is(404))))
		.pause(10000 milliseconds) // Pause 10000 milliseconds

		.exec(http("SearchFlight") // Rename recorded Request
			.post("/mercuryreservation2.php")
			.headers(headers_21)
			.formParam("tripType", "roundtrip")
			.formParam("passCount", "2")
			.formParam("fromPort", "Frankfurt")
			.formParam("fromMonth", "3")
			.formParam("fromDay", "6")
			.formParam("toPort", "London")
			.formParam("toMonth", "3")
			.formParam("toDay", "7")
			.formParam("servClass", "Coach")
			.formParam("airline", "No Preference")
			.formParam("findFlights.x", "80")
			.formParam("findFlights.y", "10")
			.resources(http("request_30")
			.get("/images/nav/logo.gif"),
            http("request_31")
			.get("/images/nav/html.gif"),
            http("request_32")
			.get("/images/nav/boxad1.gif"),
            http("request_33")
			.get("/images/banner2.gif"),
            http("request_34")
			.get("/images/forms/continue.gif"),
            http("request_35")
			.get("/images/masts/mast_selectflight.gif")
			.headers(headers_11),
            http("request_36")
			.get("/images/spacer.gif")
			.headers(headers_11)
			.check(status.is(404)))
			.check(bodyString saveAs "responseDataForCorrelation") // Correlate 'outFlight' Parameter -- Save the response body to check for 'outFlight' Parameter
			.check(css("input[name='outFlight']", "value").saveAs("Corr_outFlight")) // Correlate 'outFlight' Parameter -- save the value of 'outFlight'
		)

		.exec { // Correlate 'outFlight' Parameter -- Print the response body to check for 'outFlight' Parameter
			session =>
				println(session("responseDataForCorrelation").as[String])
				session
		}

		.exec {// Correlate 'outFlight' Parameter -- print the value of 'outFlight'
			session =>
				println(session("Corr_outFlight").as[String])
				session
		}

		.pause(10000 milliseconds) // Pause 10000 milliseconds

		.exec(http("SelectFlight") // Rename recorded Request
			.post("/mercurypurchase.php")
			.headers(headers_21)
			.formParam("fromPort", "Frankfurt")
			.formParam("toPort", "London")
			.formParam("passCount", "2")
			.formParam("toDay", "7")
			.formParam("toMonth", "3")
			.formParam("fromDay", "6")
			.formParam("fromMonth", "3")
			.formParam("servClass", "Coach")
			.formParam("outFlight", "${Corr_outFlight}")
			//.formParam("outFlight", "Blue Skies Airlines$361$271$7:10")
			.formParam("inFlight", "Blue Skies Airlines$631$273$14:30")
			.formParam("reserveFlights.x", "48")
			.formParam("reserveFlights.y", "15")
			.resources(http("request_38")
			.get("/images/nav/logo.gif"),
            http("request_39")
			.get("/images/nav/html.gif"),
            http("request_40")
			.get("/images/nav/boxad1.gif"),
            http("request_41")
			.get("/images/banner2.gif"),
            http("request_42")
			.get("/images/masts/mast_book.gif")
			.headers(headers_11),
            http("request_43")
			.get("/images/spacer.gif")
			.headers(headers_11)
			.check(status.is(404)),
            http("request_44")
			.get("/images/forms/purchase.gif")
			.headers(headers_11),
            http("request_45")
			.get("/black")
			.headers(headers_11)
			.check(status.is(404))))

		.pause(10000 milliseconds) // Pause 10000 milliseconds

		.feed(csvFeeder, 2) // CSV Feeder for Passengers' detail
		.exec(http("BookFlight") // Rename recorded Request
			.get("/images/nav/logo.gif")
			.resources(http("request_47")
			.get("/images/nav/html.gif"),
            http("request_48")
			.get("/images/nav/boxad1.gif"),
            http("request_49")
			.get("/images/banner2.gif"),
            http("request_50")
			.post("/mercurypurchase2.php")
			.headers(headers_21)
			.formParam("outFlightName", "Blue Skies Airlines")
			.formParam("outFlightNumber", "361")
			.formParam("outFlightPrice", "271")
			.formParam("outFlightTime", "7:10")
			.formParam("inFlightName", "Blue Skies Airlines")
			.formParam("inFlightNumber", "631")
			.formParam("inFlightPrice", "273")
			.formParam("inFlightTime", "14:30")
			.formParam("fromPort", "Frankfurt")
			.formParam("toPort", "London")
			.formParam("passCount", "2")
			.formParam("toDay", "7")
			.formParam("toMonth", "3")
			.formParam("fromDay", "6")
			.formParam("fromMonth", "3")
			.formParam("servClass", "Coach")
			.formParam("subtotal", "1088")
			.formParam("taxes", "89")
			.formParam("passFirst0", "${Fname1}") // CSV Feeder for Passengers' detail
			.formParam("passLast0", "${Lname1}") // CSV Feeder for Passengers' detail
			.formParam("pass.0.meal", "")
			.formParam("passFirst1", "${Fname2}") // CSV Feeder for Passengers' detail
			.formParam("passLast1", "${Lname2}") // CSV Feeder for Passengers' detail
			.formParam("pass.1.meal", "")
			.formParam("creditCard", "AX")
			.formParam("creditnumber", "")
			.formParam("cc_exp_dt_mn", "None")
			.formParam("cc_exp_dt_yr", "None")
			.formParam("cc_frst_name", "")
			.formParam("cc_mid_name", "")
			.formParam("cc_last_name", "")
			.formParam("billAddress1", "1325 Borregas Ave.")
			.formParam("billAddress2", "")
			.formParam("billCity", "Sunnyvale")
			.formParam("billState", "CA")
			.formParam("billZip", "94089")
			.formParam("billCountry", "215")
			.formParam("delAddress1", "1325 Borregas Ave.")
			.formParam("delAddress2", "")
			.formParam("delCity", "Sunnyvale")
			.formParam("delState", "CA")
			.formParam("delZip", "94089")
			.formParam("delCountry", "215")
			.formParam("buyFlights.x", "53")
			.formParam("buyFlights.y", "7"),
            http("request_51")
			.get("/images/masts/mast_confirmation.gif")
			.headers(headers_11),
            http("request_52")
			.get("/images/printit.gif")
			.headers(headers_11),
            http("request_53")
			.get("/images/forms/backtoflights.gif")
			.headers(headers_11),
            http("request_54")
			.get("/images/spacer.gif")
			.headers(headers_11)
			.check(status.is(404)),
            http("request_55")
			.get("/images/forms/Logout.gif")
			.headers(headers_11),
            http("request_56")
			.get("/images/forms/home.gif")
			.headers(headers_11),
            http("request_57")
			.get("/black")
			.headers(headers_11)
			.check(status.is(404))))
		.pause(10000 milliseconds) // Pause 10000 milliseconds

		.exec(http("Logout") // Rename recorded Request
			.get("/mercurysignoff.php")
			.headers(headers_0)
			.resources(http("request_59")
			.get("/images/nav/logo.gif"),
            http("request_60")
			.get("/images/nav/html.gif"),
            http("request_61")
			.get("/images/nav/boxad1.gif"),
            http("request_62")
			.get("/images/banner2.gif"),
            http("request_63")
			.get("/images/masts/mast_signon.gif")
			.headers(headers_26),
            http("request_64")
			.get("/images/forms/submit.gif")
			.headers(headers_26),
            http("request_65")
			.get("/images/spacer.gif")
			.headers(headers_11)
			.check(status.is(404)),
            http("request_66")
			.get("/black")
			.headers(headers_11)
			.check(status.is(404))))

	setUp(scn.inject(atOnceUsers(1))).protocols(httpProtocol)
  	.uniformPauses(10.00) // Global Pause Configuration - Uniform Pause +/- 10%
  	.assertions(
			forAll.responseTime.max.lt(60), // Assert - Response Time less than 60 seconds
			global.failedRequests.percent.lt(5)) // Assert - failed request should be less than 5%
}