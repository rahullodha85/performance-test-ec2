package hbc_commonStack.chains.checkout

import hbc_newLT.newLT_base.newLT
import hbc_commonStack.base.commonStack
import io.gatling.core.Predef._
import io.gatling.http.Predef._

/**
  * Created by aindana on 3/14/2016.
  */
object RemoveItem extends commonStack {
  // val ProductID = jsonFile("src/test/resources/data/ProductID.json").circular
  lazy val checkout = getbasehttp() + "/checkout/checkout.jsp"

  def removeItem = group("RemoveItem_Transaction") {
          exec(http("add_saks_suggests_item_service_product_array")
          .post(checkout)
              .body(StringBody("bmForm=remove_cart_item_service&cartItemId=${cartItemID}"))
            //.check(regex("""cartItemId":"(\d+)""").findAll.saveAs("cartItemIDList"))
            .check(regex("""cartItemId":"(\d+)""").count.saveAs("cartItemIDCount"))
            .check(status.is(200))

        )


        .exec(session => {
          insertGroupResponseTimeToInfluxDB(session toString, "RemoveItem")
          insertGroupResponseTimeToNewRelic(session toString, "RemoveItem")
          session

        })

    }
}


