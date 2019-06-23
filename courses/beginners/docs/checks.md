# Checks

- Verify that the response to a request is correct

  - Verify HTTP status e.g

    **.check(status)**

  - Verify page location e.g

    **.check(currentLocation i.e. url)**

    **.check(currentLocationRegex(pattern))**

  - Verify HTTP response body e.g.

    **.check(bodyString)**

    **.check(css(expression, attribute))**

    **.check(substring(expression))**

  - Extract single result such as

    **.find**

  - Extract multiple results such as

    **.find(occurrence)**

    **.findAll**

    **.findRandom**

    **.count**

  - Validate e.g.

    **.is(expected)**

    **.not(expected)**

    **.isNull**

    **.notNull**

    **.exists**

    **.notExists**

    **.in(sequence)**

- Save a part of the response (to be used later)