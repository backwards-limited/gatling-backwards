# Cache & Cookies

During a load test, it is good practice to clear cache and cookies as new users will always start a fresh and we want our load test of multiple users running in parallel to all start from a fresh i.e. load test real live scenarios.

To clear the cache:

```scala
.exec(flushHttpCache)
```

To clear session cookies (which simulates closing browser before restarting):

```scala
.exec(flushSessionCookies)
```

To clear all cookies:

```scala
.exec(flushCookieJar)
```

