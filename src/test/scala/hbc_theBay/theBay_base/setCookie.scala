package hbc_theBay.theBay_base

import io.gatling.core.Predef._
import io.gatling.http.Predef._

/**
  * Created by aindana on 3/14/2016.
  */
object setCookie extends theBay {
  //  val random = new util.Random
  //  val feeder = Iterator.continually(Map("email" -> (random.nextString(20) + "@foo.com")))


  val setCookieLT = group("clearing cache") {
    feed(ShippingDetails)
      .feed(BillingDetails)
        .feed(SearchString)
        .feed(changeQty)
      .exec(http("setCookie-http")
        .get("http://metrics.thebay.com/optout.html?popup=1&locale=en_US&optout=1&confirm_change=1")
        .check(status.is(200))
      )

        .exec(http("setCookie-https")
          .get("https://metrics.thebay.com//optout.html?popup=1&locale=en_US&optout=1&confirm_change=1")
          .check(status.is(200))
        )
  }
}
