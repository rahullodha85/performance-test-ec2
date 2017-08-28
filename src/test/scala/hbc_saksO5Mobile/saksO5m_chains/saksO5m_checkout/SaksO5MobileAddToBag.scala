package hbc_saksO5Mobile.saksO5m_chains.saksO5m_checkout

import hbc_saksO5Mobile.saksO5m_base.SaksO5Mobile
import io.gatling.core.Predef._
import io.gatling.http.Predef._

/**
  * Created by aindana on 3/14/2016.
  */
object SaksO5MobileAddToBag extends SaksO5Mobile {
  // val ProductID = jsonFile("src/test/resources/data/ProductID.json").circular
  lazy val checkout = getbasehttp() + "/checkout/checkout.jsp"
  lazy val saksBag = getbasehttp() + "/checkout/SaksBag.jsp"

  def AddToBag(ItemQty:String="1") = group("AddToBag_Transaction") {
        exec(http("add_saks_suggests_item_service_product_array")
          .post(checkout)
          .formParam("bmForm", "add_saks_suggests_item_service_product_array")
          .formParam("productCode", "${ProductID}")
          .formParam("itemQuantity", ItemQty)
          .formParam("sku_id", "${sku_id}")
          .formParam("type", "REQUEST_ADD_TO_BAG")
          .check(status.is(200))
          .check(regex("""displayAddToBag":true""").count.saveAs("AddToBag_valid"))
          //    .check(bodyString.saveAs("fullBody1"))


        )


          .doIf(session => session("AddToBag_valid").as[Int] != 0) {
            exec(http("saksbaginsert")
              .get(saksBag)
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


