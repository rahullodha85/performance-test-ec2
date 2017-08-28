package hbc_theBay.thebay_chains.theBay_Checkout

import hbc_theBay.theBay_base.theBay
import io.gatling.core.Predef._
import io.gatling.http.Predef._

/**
  * Created by aindana on 3/14/2016.
  */
object theBayBilling extends theBay{
  lazy val url1 = getbasehttps() + "/webapp/wcs/stores/servlet/HBCPIAddCmd"
  lazy val url2 = getbasehttps() + "/webapp/wcs/stores/servlet/AjaxTenderType"
  lazy val url3 = getbasehttps() + "/webapp/wcs/stores/servlet/TraditionalAjaxShippingDetailsURL"
  lazy val url4 = getbasehttps() + "/webapp/wcs/stores/servlet/CurrentOrderInformationView"
  lazy val url5 = getbasehttps() + "/webapp/wcs/stores/servlet/CreditAmount"
  lazy val url6 = getbasehttps() + "/webapp/wcs/stores/servlet/HBCPIAddCmd"
  lazy val url7 = getbasehttps() + "/webapp/wcs/stores/servlet/AjaxOrderProcessServiceOrderPrepare"
  lazy val url8 = getbasehttps() + "/webapp/wcs/stores/servlet/OrderShippingBillingSummaryView"

  def Billing = group("Billing_Transaction") {
      exec(http("HBCPIAddCmd")
        .post(url1)
          .formParam("storeId","${StoreID}")
        .formParam("catalogId","${catalogID}")
        .formParam("langId","-24")
        .formParam("orderId","${orderId}")
        .formParam("payMethodId","${CardBrand}")
        .formParam("ordersubmit","0")
        .formParam("checkorder","0")
        .formParam("url","HBCPIAddCmd")
        .formParam("requesttype","ajax")
        .check(status.is(200))

        //.check(bodyString.saveAs("test"))

            )

          .exec(http("AjaxTenderType")
              .post(url2)
            .formParam("orderId","${orderId}")
            .formParam("requesttype","ajax")
            .check(status.is(200))

            //    .check(bodyString.saveAs("fullBody"))
          )


        .exec(http("TraditionalAjaxShippingDetailsURL")
          .post(url3)
          .queryParam("storeId","${StoreID}")
          .queryParam("catalogId","${catalogID}")
          .queryParam("langId","-24")
          .formParam("shipmentDetailsArea","update")
          .formParam("objectId","")
          .formParam("requesttype","ajax")
          .check(status.is(200))

        )

        .exec(http("CurrentOrderInformationView")
          .post(url4)
          .queryParam("storeId","${StoreID}")
          .queryParam("catalogId","${catalogID}")
          .queryParam("langId","-24")
          .body(StringBody("objectId=&requesttype=ajax"))
          .check(status.is(200))
        )



        .exec(http("CreditAmount")
          .post(url5)
          .queryParam("piAmount","${total}")
          .queryParam("catalogId","${catalogID}")
          .queryParam("langId","-24")
          .queryParam("CurrencySymbolToFormat","$")
          .queryParam("paymentAreaNumber","1")
          .queryParam("storeId","${StoreID}")
          .queryParam("amount","${total}")
          .queryParam("refresh","R")
          .body(StringBody("objectId=&requesttype=ajax"))
          .check(status.is(200))

        )

        .exec(http("CreditAmount_2")
          .post(url5)
          .queryParam("piAmount","${total}")
          .queryParam("catalogId","${catalogID}")
          .queryParam("langId","-24")
          .queryParam("CurrencySymbolToFormat","$")
          .queryParam("paymentAreaNumber","1")
          .queryParam("storeId","${StoreID}")
          .queryParam("amount","${total}")
          .queryParam("refresh","R")
          .body(StringBody("objectId=&requesttype=ajax"))
          .check(status.is(200))

        )

        .exec(http("HBCPIAddCmd")
          .post(url6)
          .formParam("storeId","${StoreID}")
          .formParam("catalogId","${catalogID}")
          .formParam("langId","-24")
          .formParam("orderId","${orderId}")
          .formParam("payMethodId","${CardBrand}")
          .formParam("expire_month","${CardMonth}")
          .formParam("expire_year","${CardYear}")
          .formParam("account","${CardNum}")
          .formParam("cc_cvc","${CVV}")
          .formParam("url","HBCPIAddCmd")
          .formParam("cc_brand","${CardBrand}")
          .formParam("billing_address_id","${addressID}")
          .formParam("CCpiAmount","${total}")
          .formParam("checkorder","0")
          .formParam("requesttype","ajax")
          .check(status.is(200))


        )



        .exec(http("AjaxOrderProcessServiceOrderPrepare")
          .post(url7)
          .formParam("storeId","${StoreID}")
          .formParam("catalogId","${catalogID}")
          .formParam("langId","-24")
          .formParam("orderId","${orderId}")
          .formParam("OrderTotal","${total}")
          .formParam("notifyMerchant","1")
          .formParam("notifyShopper","1")
          .formParam("notifyOrderSubmitted","1")
          .formParam("requesttype","ajax")
          .check(status.is(200))
        )





        .exec(http("OrderShippingBillingSummaryView")
          .get(url8)
          .queryParam("storeId","${StoreID}")
          .queryParam("catalogId","${catalogID}")
          .queryParam("langId","-24")
          .queryParam("orderId","${orderId}")
          .queryParam("shipmentTypeId","1")
          .check(status.is(200))


        )



        .exec(session => {

          insertGroupResponseTimeToInfluxDB(session toString, "Billing")
          insertGroupResponseTimeToNewRelic(session toString, "Billing")
          session

        })

  }

  }
