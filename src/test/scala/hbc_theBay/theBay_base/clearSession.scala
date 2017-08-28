package hbc_theBay.theBay_base

import io.gatling.core.Predef._
import io.gatling.http.Predef._

/**
  * Created by aindana on 3/14/2016.
  */
object clearSession extends theBay {
  //  val random = new util.Random
  //  val feeder = Iterator.continually(Map("email" -> (random.nextString(20) + "@foo.com")))


  def clearSession = group("clearing cache") {
    exec(flushCookieJar)
      .exec(flushSessionCookies)
      .exec(flushHttpCache)

      .exec(session => {

        session.set("numOfItems", 0).set("PDPincementer",0)

      })

    /*  .exec(addCookie(Cookie("testAkamai", "true")
        .withDomain(".lordandtaylor.com")
        .withPath("/")))*/

  }
}
