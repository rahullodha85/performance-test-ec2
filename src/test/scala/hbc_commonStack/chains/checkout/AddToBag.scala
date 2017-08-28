package hbc_commonStack.chains.checkout

import hbc_commonStack.base.commonStack
import io.gatling.core.Predef._
import io.gatling.http.Predef._

/**
  * Created by aindana on 3/14/2016.
  */
object AddToBag extends commonStack {
  // val ProductID = jsonFile("src/test/resources/data/ProductID.json").circular
  lazy val checkout = getbasehttp() + "/checkout/checkout.jsp"

  def AddToBag(ItemQty:String="1") = group("AddToBag_Transaction") {
    feed(changeQty)
      .feed(ShippingDetails)
      .exec(http("add_saks_suggests_item_service_product_array")
        .post(checkout)
        .formParam("bmForm","add_saks_suggests_item_service_product_array")
        .formParam("itemQuantity",ItemQty)
        .formParam("productCode","${ProductID}")
        .formParam("sku_id","${sku_id}")
        .formParam("type","REQUEST_ADD_TO_BAG")
        .check(regex("""addToBagResults""").exists)
        .check(status.is(200))
        .check(regex("""displayAddToBag":true""").count.saveAs("AddToBag_valid")))

      .exec(http("add_saks_suggests_item_service_product_array")
      .get(getbasehttp() + "/v1/cart-service/viewcart")
      .check(status.is(200)))

      .exec(session => {
        session.set("numOfItems", session("numOfItems").as[Int] + 1)

      })

        .exec(session => {
          insertGroupResponseTimeToInfluxDB(session toString, "AddToBag")
          insertGroupResponseTimeToNewRelic(session toString, "AddToBag")
          session

        })

    }
}


