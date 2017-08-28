package hbc_newLT.newLT_chains.newLT_checkout

import hbc_newLT.newLT_base.newLT
import io.gatling.core.Predef._
import io.gatling.http.Predef._

/**
  * Created by aindana on 3/14/2016.
  */
object newLTPromo extends newLT {
  // val ProductID = jsonFile("src/test/resources/data/ProductID.json").circular
  lazy val checkout = getbasehttp() + "/checkout/checkout.jsp"

  def applyPromo = group("ApplyPromo_Transaction") {
          exec(http("add_saks_suggests_item_service_product_array")
          .post(checkout)
              .body(StringBody("bmForm=applyPromoSaksBag&promoCode=cyber"))
            .check(status.is(200))

        )


        .exec(session => {
          insertGroupResponseTimeToInfluxDB(session toString, "ApplyPromo")
          insertGroupResponseTimeToNewRelic(session toString, "ApplyPromo")
          session

        })

    }
}


