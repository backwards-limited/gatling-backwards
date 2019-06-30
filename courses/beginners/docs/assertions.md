# Assertions

Assertions are evaluated after running a simulation.

Scope > Statistic > Metric > Condition

## Scope

- global
- forAll
- details (path)

## Statistic

- responseTime
- requestsPerSec
- allRequests
- failedRequests
- successfulRequests

## Metric

- min
- max
- mean
- stdDev
- percentile1
- percentile2
- percentile3
- percentile4
- percentile (value: Double)
- percentage
- count

## Condition

- lt (threshold)
- lte (threshold)
- gt (threshold)
- gte (threshold)
- between (thresholdMin, thresholdMax)
- between (thresholdMin, thresholdMax, inclusive = false)
- is (value)
- in (sequence)

## Assertions Evaluation Example

[Simulation2.scala](../src/test/scala/com/backwards/gatling/Simulation2.scala) has the following setup:

```scala
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
```

When the assertions are evaluated, the Gatling report finishes with (where times are in milliseconds):

```bash
Global: max of response time is less than 1000.0 : false
Homepage: max of response time is less than 1000.0 : false
SearchFlight: max of response time is less than 1000.0 : true
SelectFlight: max of response time is less than 1000.0 : true
BookFlight: max of response time is less than 1000.0 : true
BookFlight Redirect 1: max of response time is less than 1000.0 : true
BookFlight: max of response time is less than 1000.0 : true
Global: percentage of successful events is 100.0 : true
```

