package hbc_saksO5Mobile.saksO5m_chains.saksO5m_checkout

import BaseTest.BaseTest
import hbc_saksO5Mobile.saksO5m_base.SaksO5Mobile
import io.gatling.core.Predef._
import io.gatling.http.Predef._

/**
  * Created by aindana on 3/14/2016.
  */
object SaksO5MobileShoppingBag extends SaksO5Mobile{
 // val ProductID = jsonFile("src/test/resources/data/ProductID.json").circular
  lazy val bag = getbasehttps() + "/checkout/SaksBag.jsp"

  def ShoppingBag = group("ShoppingBag_Transaction") {
    exec(http("shopping bag")
          .get(bag)
              .check(status.is(200))
        )

      .exec(http("shopping bag_2")
        .post(bag)
        .body(StringBody("bmForm=initialize_saks_bag_service"))
        .check(regex("""cartItemId":"(\d+)""").saveAs("cartItemID"))
        .check(status.is(200))
      )

      .exec(session => {
        insertGroupResponseTimeToInfluxDB(session toString, "ShoppingBag")
        insertGroupResponseTimeToNewRelic(session toString, "ShoppingBag")

        session

      })
  }

  }
