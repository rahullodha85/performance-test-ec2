package hbc_commonStack.chains.checkout

import hbc_newLT.newLT_base.newLT
import hbc_commonStack.base.commonStack
import io.gatling.core.Predef._
import io.gatling.http.Predef._

/**
  * Created by aindana on 3/14/2016.
  */
object AddToBag_old extends commonStack {
  // val ProductID = jsonFile("src/test/resources/data/ProductID.json").circular
  lazy val checkout = getbasehttps() + "/checkout/checkout.jsp"

  def AddToBag_old(ItemQty:String="1") = group("AddToBag_Transaction") {
    feed(changeQty)
      .feed(ShippingDetails)
      .exec(session => {

        val checkout2 = getbasehttp() + "/include/saksbaginsert.jsp?PRODUCT%3C%3Eprd_id=" + session("prd_id").as[String] + "&FOLDER%3C%3Efolder_id=" + session("folder_id").as[String] + "&bmUID=" + session("bmUID").as[String] + "&num_of_items_added=" +  ItemQty

        session.set("checkoutUrl", checkout2)

    })

          .exec(http("add_saks_suggests_item_service_product_array")
          .post(checkout)
              .header("Referer","${PDPLink}")
              .body(StringBody("bmForm=add_saks_suggests_item_service_product_array&sku_id=${sku_id}&productCode=${ProductID}&itemQuantity=1&type=REQUEST_ADD_TO_BAG"))
     /*     .formParam("bmForm", "add_saks_suggests_item_service_product_array")
          .formParam("productCode", "${ProductID}")
          .formParam("itemQuantity", "${ItemQty}")
          .formParam("sku_id", "${sku_id}")
          .formParam("type", "REQUEST_ADD_TO_BAG")*/
            .check(regex("""addToBagResults""").exists)
          .check(status.is(200))
          .check(regex("""displayAddToBag":true""").count.saveAs("AddToBag_valid"))

          //    .check(bodyString.saveAs("fullBody1"))


        )


        .doIf(session => session("AddToBag_valid").as[Int] != 0) {
              exec(http("saksbaginsert")
                .post("${checkoutUrl}")
                .body(StringBody("&num_of_items_added="+ItemQty))
                .check(regex("""id="status(\d+)" class""").saveAs("cartID"))
                .check(regex("""id="status(\d+)" class""").count.saveAs("cartItemIdCount"))
                .check(status.is(200))
              )

                .exec(session => {
                  session.set("numOfItems", session("numOfItems").as[Int] + 1)

                })

          }

        .exec(session => {
          insertGroupResponseTimeToInfluxDB(session toString, "AddToBag")
          insertGroupResponseTimeToNewRelic(session toString, "AddToBag")
          session

        })

    }
}


