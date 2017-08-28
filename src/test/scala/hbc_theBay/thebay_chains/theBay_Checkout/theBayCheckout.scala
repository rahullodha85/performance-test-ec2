package hbc_theBay.thebay_chains.theBay_Checkout

import hbc_theBay.theBay_base.theBay
import io.gatling.core.Predef._
import io.gatling.http.Predef._

/**
  * Created by aindana on 3/14/2016.
  */
object theBayCheckout extends theBay{
  lazy val url1 = getbasehttps() + "/webapp/wcs/stores/servlet/HBCAjaxUpdateXordersCmd"
  def Checkout = group("Checkout_${numOfItems}Items_Transaction") {
      exec(http("ShopCartProcessCmd")
        .get(getbasehttps() + "/webapp/wcs/stores/servlet/OrderShippingBillingCmd?catalogId=${catalogID}&langId=-24&storeId=${StoreID}&shipmentType=single&country=CA")
        .check(regex("""name="authToken" value="(.*?)"""").saveAs("authToken"))
        //.check(bodyString.saveAs("test"))

            )

          .exec(http("HBCAjaxUpdateXordersCmd")
              .post(url1)
              .body(StringBody("bopisEligiblity=N&selectedStoreId=&orderIdForBopis=${orderId}&checkOutRemoveOrderAttr=Y&requesttype=ajax"))
            .check(status.is(200))
             //.check(bodyString.saveAs("test"))

          )

        .exec(http("order-shipping-billing-details.css")
          .get(getbasehttps() + "/wcsstore/TheBay/css/dist/order-shipping-billing-details.css")
          .check(status.is(200))
          //.check(bodyString.saveAs("test"))

        )

        .exec(session => {

          session.set("nickname", System currentTimeMillis)


        })

        .exec(session => {

          insertGroupResponseTimeToInfluxDB(session toString, "Checkout")
          insertGroupResponseTimeToNewRelic(session toString, "Checkout")
          session

        })

  }

  }
