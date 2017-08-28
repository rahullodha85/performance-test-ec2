package hbc_commonStack.base

import io.gatling.core.Predef._
import io.gatling.http.Predef._

/**
  * Created by aindana on 3/14/2016.
  */
object setCookie extends commonStack {
  //  val random = new util.Random
  //  val feeder = Iterator.continually(Map("email" -> (random.nextString(20) + "@foo.com")))


  val setCookieSaksO5 = group("clearing cache") {
    feed(ShippingDetails)
      .feed(BillingDetails)
        .feed(SearchString)
        .feed(Products)
        .exec(http("setCookie-http")
      .get("http://sitectlyst.saksfifthavenue.com/optout.html?popup=1&locale=en_US&optout=1&confirm_change=1")
      .check(status.is(200))
    )

      .exec(http("setCookie-https")
        .get("https://sitectlyst.saksfifthavenue.com/optout.html?popup=1&locale=en_US&optout=1&confirm_change=1")
        .check(status.is(200))
      )
  }
}
