package hbc_saksO5Mobile.saksO5m_chains.saksO5m_checkout

import BaseTest.BaseTest
import hbc_saksO5Mobile.saksO5m_base.SaksO5Mobile
import io.gatling.core.Predef._
import io.gatling.http.Predef._

/**
  * Created by aindana on 3/14/2016.
  */
object SaksO5MobileCheckout extends SaksO5Mobile{
 // val ProductID = jsonFile("src/test/resources/data/ProductID.json").circular
  lazy val checkout = getbasehttps() + "/checkout/checkout.jsp"

  def Checkout = group("Checkout${numOfItems}Items_Transaction") {
    exec(http("login_as_guest_user")
      .post(checkout)
      .body(StringBody("bmForm=login_as_guest_user&LOGIN<>userid="))
      .check(status.is(200))

      //   .check(regex("""soldOut":false""").exists)

    )

      .exec(http("get checkout")
          .get(checkout)
              .check(status.is(200))
        )

      .exec(http("continue_to_checkout_service")
        .post(checkout)
        .body(StringBody("bmForm=continue_to_checkout_service"))
        .check(status.is(200))
      )

      .exec(http("get_option_list_service titles")
        .post(checkout)
        .body(StringBody("bmForm=get_option_list_service&listName=titles"))
        .check(status.is(200))
      )

      .exec(http("get_option_list_service states")
        .post(checkout)
        .body(StringBody("bmForm=get_option_list_service&listName=states"))
        .check(status.is(200))
      )

      .exec(session => {
        insertGroupResponseTimeToInfluxDB(session toString, "Checkout_" + session("numOfItems").as[String] + "Items")
        insertGroupResponseTimeToNewRelic(session toString, "Checkout_" + session("numOfItems").as[String] + "Items")

        session

      })

  }

  }
