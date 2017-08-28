package hbc_newLT.newLT_chains.newLT_checkout

import hbc_newLT.newLT_base.newLT
import io.gatling.core.Predef._
import io.gatling.http.Predef._

/**
  * Created by aindana on 3/14/2016.
  */
object newLTAddToBag extends newLT {
  // val ProductID = jsonFile("src/test/resources/data/ProductID.json").circular
  lazy val checkout = getbasehttp() + "/checkout/checkout.jsp"

  def AddToBag(ItemQty:String="1") = group("AddToBag_Transaction") {
    feed(changeQty)
      .feed(ShippingDetails)
      .exec(session => {

        val checkout2 = getbasehttp() + "/include/saksbaginsert.jsp?PRODUCT%3C%3Eprd_id=" + session("prd_id").as[String] + "&FOLDER%3C%3Efolder_id=" + session("folder_id").as[String] + "&bmUID=" + session("bmUID").as[String] + "&num_of_items_added=" +  ItemQty

        session.set("checkoutUrl", checkout2)

    })

          .exec(http("add_saks_suggests_item_service_product_array")
          .post(checkout)
          .formParam("bmForm", "add_saks_suggests_item_service_product_array")
          .formParam("productCode", "${ProductID}")
          .formParam("itemQuantity", ItemQty)
          .formParam("sku_id", "${sku_id}")
          .formParam("type", "REQUEST_ADD_TO_BAG")
          .check(status.is(200))
          .check(regex("""displayAddToBag":true""").count.saveAs("AddToBag_valid"))
              .check(bodyString.saveAs("fullBody1"))


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
          if(session("fullBody1").as[String].contains("java.lang.NullPointerException")) println(session("fullBody1").as[String])
          insertGroupResponseTimeToInfluxDB(session toString, "AddToBag")
          insertGroupResponseTimeToNewRelic(session toString, "AddToBag")
          session

        })

    }
}


