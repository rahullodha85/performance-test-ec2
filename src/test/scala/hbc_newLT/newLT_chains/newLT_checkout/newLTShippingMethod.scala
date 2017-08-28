package hbc_newLT.newLT_chains.newLT_checkout

import hbc_newLT.newLT_base.newLT
import io.gatling.core.Predef._
import io.gatling.http.Predef._

/**
  * Created by aindana on 3/14/2016.
  */
object newLTShippingMethod extends newLT {
  // val ProductID = jsonFile("src/test/resources/data/ProductID.json").circular
  lazy val checkout = getbasehttp() + "/checkout/checkout.jsp"

  def shippingMethod = group("ShippingMethod_Transaction") {
    feed(ShippingDetails)
        .exec(http("add_saks_suggests_item_service_product_array")
          .post(checkout)
              .body(StringBody("bmForm=update_cart_summary_service&zipCode=${Zip}&selectedMethodId=${ShippingMethod}"))
            .check(status.is(200))

        )


        .exec(session => {
          insertGroupResponseTimeToInfluxDB(session toString, "ShippingMethod")
          insertGroupResponseTimeToNewRelic(session toString, "ShippingMethod")
          session

        })

    }
}


