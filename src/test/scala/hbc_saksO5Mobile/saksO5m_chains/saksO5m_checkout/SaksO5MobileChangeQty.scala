package hbc_saksO5Mobile.saksO5m_chains.saksO5m_checkout

import BaseTest.BaseTest
import hbc_saksO5Mobile.saksO5m_base.SaksO5Mobile
import io.gatling.core.Predef._
import io.gatling.http.Predef._

/**
  * Created by aindana on 3/14/2016.
  */
object SaksO5MobileChangeQty extends SaksO5Mobile{
 // val ProductID = jsonFile("src/test/resources/data/ProductID.json").circular
 lazy val checkout = getbasehttps() + "/checkout/checkout.jsp"

  def ChangeQty(ChangeItemQty:Int=1) = group("ChangeQty_Transaction") {
    exec(session => {
      session.set("ChangeItemQty",ChangeItemQty)

    })

      .exec(http("cart look up")
          .post(checkout)
        .body(StringBody("bmForm=cart_item_quick_look_service&cartItemId=${cartItemID}"))
              .check(status.is(200))
        )

      .exec(http("change qty")
        .post(checkout)
        .body(StringBody("cartItemId=${cartItemID}&itemQuantity=${ChangeItemQty}&sku_id=${sku_id}&bmForm=edit_cart_item_service"))
        .check(status.is(200))
      )

      .exec(session => {
        insertGroupResponseTimeToInfluxDB(session toString, "ChangeQty")
        insertGroupResponseTimeToNewRelic(session toString, "ChangeQty")

        session

      })

  }

  }
