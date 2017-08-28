package hbc_newLT.newLT_chains.newLT_checkout

import BaseTest.BaseTest
import hbc_newLT.newLT_base.newLT
import io.gatling.core.Predef._
import io.gatling.http.Predef._

/**
  * Created by aindana on 3/14/2016.
  */
object newLTChangeQty extends newLT{
//  val random = new util.Random
//  val feeder = Iterator.continually(Map("email" -> (random.nextString(20) + "@foo.com")))

  lazy val chkUrl = getbasehttps() + "/checkout/checkout.jsp"

  lazy val update_cart_item_quantity_service = "update_cart_item_quantity_service"
  def ChangeQty = group("ChangeQty_Transaction") {
    feed(changeQty)
      .exec(http(update_cart_item_quantity_service)
      .post(chkUrl)
      .body(StringBody("bmForm=update_cart_item_quantity_service&cartItemId=${cartID}&itemQuantity=${ChangeItemQty}"))
        .check(regex(""""quantity":${ChangeItemQty}""").exists)
      .check(status.is(200))
      )

      .exec(session => {
        insertGroupResponseTimeToInfluxDB(session toString, "ChangeQty")
        insertGroupResponseTimeToNewRelic(session toString, "ChangeQty")
        session

      })



    }



}
