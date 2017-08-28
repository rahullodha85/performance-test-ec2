package hbc_saksO5Mobile.saksO5m_base

import BaseTest.BaseTest
import io.gatling.core.Predef._
import io.gatling.http.Predef._

/**
  * Created by aindana on 3/14/2016.
  */
object setCookie extends SaksO5Mobile {
  //  val random = new util.Random
  //  val feeder = Iterator.continually(Map("email" -> (random.nextString(20) + "@foo.com")))


  def setCookieSaks = group("clearing cache") {

    exec(http("setCookie-http")
      .get("http://sitectlyst.saksfifthavenue.com/optout.html?popup=1&locale=en_US&optout=1&confirm_change=1")
      .check(status.is(200))
    )

      .exec(http("setCookie-https")
        .get("https://sitectlyst.saksfifthavenue.com/optout.html?popup=1&locale=en_US&optout=1&confirm_change=1")
        .check(status.is(200))
      )
  }
}
