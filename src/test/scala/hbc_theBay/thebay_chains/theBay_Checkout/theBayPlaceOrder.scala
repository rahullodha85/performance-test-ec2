package hbc_theBay.thebay_chains.theBay_Checkout

import BaseTest.BaseTest
import hbc_theBay.theBay_base.theBay
import io.gatling.core.Predef._
import io.gatling.http.Predef._

/**
  * Created by aindana on 3/14/2016.
  */
object theBayPlaceOrder extends theBay{

  lazy val placeOrderUrl = getbasehttps() + "/webapp/wcs/stores/servlet/AjaxOrderProcessServiceOrderSubmit"
  lazy val OrderShippingBillingConfirmationView = getbasehttps()  + "/webapp/wcs/stores/servlet/OrderShippingBillingConfirmationView"
  lazy val HBCGetPurolatorHubAddressCmd = getbasehttps()  + "/webapp/wcs/stores/servlet/HBCGetPurolatorHubAddressCmd"
  def PlaceOrder = group("SubmitOrder_Transaction") {
      exec(http("AjaxOrderProcessServiceOrderSubmit")
        .post(placeOrderUrl)
          .formParam("storeId","${StoreID}")
        .formParam("catalogId","${catalogID}")
        .formParam("langId","-24")
        .formParam("orderId","${orderId}")
        .formParam("notifyMerchant","1")
        .formParam("notifyShopper","1")
        .formParam("notifyOrderSubmitted","1")
        .formParam("notify_EMailSender_recipient","")
        .formParam("requesttype","ajax")
        .check(status.is(200))
        //.check(bodyString.saveAs("test"))
        .check(regex("""orderId": \["(\d+)""").saveAs("orderId"))
            )

        .exec(http("OrderShippingBillingConfirmationView")
          .get(OrderShippingBillingConfirmationView)
          .queryParam("storeId","${StoreID}")
          .queryParam("catalogId","${catalogID}")
          .queryParam("langId","-24")
          .queryParam("orderId","${orderId}")
          .queryParam("shipmentTypeId","1")
          .check(status.is(200))

        )

        .exec(http("HBCGetPurolatorHubAddressCmd")
          .post(HBCGetPurolatorHubAddressCmd)
          .body(StringBody("postalCode=${Zip}&requesttype=ajax"))
          .check(status.is(200))

        )


        .exec(session => {

          println("ORDER NUMBER: " + session("orderId").as[String])
          insertGroupResponseTimeToInfluxDB(session toString, "PlaceOrder")
          insertGroupResponseTimeToNewRelic(session toString, "PlaceOrder")
          session

        })

  }

  }
