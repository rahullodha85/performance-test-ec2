package hbc_theBay.thebay_chains.theBay_Checkout

import hbc_theBay.theBay_base.theBay
import io.gatling.core.Predef._
import io.gatling.http.Predef._

/**
  * Created by aindana on 3/14/2016.
  */
object theBayShipping extends theBay{
  lazy val url1 = getbasehttps() + "/webapp/wcs/stores/servlet/HBCStorefrontAssetStore/qas/qas_proxy.jsp"
  lazy val url2 = getbasehttps() + "/webapp/wcs/stores/servlet/AjaxPersonChangeServiceAddressAdd"
  lazy val url3 = getbasehttps() + "/webapp/wcs/stores/servlet/AjaxOrderChangeServiceShipInfoUpdate"
  lazy val url4 = getbasehttps() + "/webapp/wcs/stores/servlet/HBCPIRemoveCmd"
  lazy val url5 = getbasehttps() + "/webapp/wcs/stores/servlet/CreditAmount"
  lazy val url6 = getbasehttps() + "/webapp/wcs/stores/servlet/GC1Amount"
  lazy val url7 = getbasehttps() + "/webapp/wcs/stores/servlet/AjaxTenderType"
  lazy val url8 = getbasehttps() + "/webapp/wcs/stores/servlet/GC2Amount"
  lazy val url9 = getbasehttps() + "/webapp/wcs/stores/servlet/TraditionalAjaxShippingDetailsURL"
  lazy val url10 = getbasehttps() + "/webapp/wcs/stores/servlet/CurrentOrderInformationView"
  def Shipping = group("Shipping_Transaction") {
    feed(ShippingDetails)
        .exec(http("qas")
        .post(url1)
          .formParam("action","search")
        .formParam("addlayout","Database layout")
        .formParam("country","CAN")
        .formParam("searchstring","${Address}|||${City}|ON|")
        //.check(bodyString.saveAs("test"))
        .check(status.is(200))
            )

          .exec(http("AjaxPersonChangeServiceAddressAdd")
              .post(url2)
            .formParam("storeId","${StoreID}")
            .formParam("catalogId","${catalogID}")
            .formParam("langId","-24")
            .formParam("status","Billing")
            .formParam("addressType","ShippingAndBilling")
            .formParam("authToken","${authToken}")
            .formParam("nickName","${nickname}")
            .formParam("phone1","2123335555")
            .formParam("zipCode","${Zip}")
            .formParam("curentstate","")
            .formParam("uKEnableFlagforEditAddress","false")
            .formParam("emptyaddress","")
            .formParam("firstName","${FirstName}")
            .formParam("lastName","${LastName}")
            .formParam("address1","${Address}")
            .formParam("address2","")
            .formParam("city","${City}")
            .formParam("country","CA")
            .formParam("state","${State}")
            .formParam("state","")
            .formParam("zipCode2","${Zip}")
            .formParam("bphone5","212")
            .formParam("bphone6","333")
            .formParam("bphone7","5555")
            .formParam("bphone8","")
            .formParam("ukphone","")
            .formParam("userField1","")
            .formParam("email1","test@aj.com")
            .formParam("fax2","")
            .formParam("fax1","${StoreID}")
            .formParam("requesttype","ajax")
            .check(regex("""addressId": \["(\d+)""").saveAs("addressID"))
            .check(regex("""userId": \["(\d+)""").saveAs("userId"))
            .check(status.is(200))
        //    .check(bodyString.saveAs("fullBody"))
          )




        .exec(http("AjaxOrderChangeServiceShipInfoUpdate")
          .post(url3)
          .body(StringBody("storeId=${StoreID}&catalogId=${catalogID}&langId=-24&orderId=.&calculationUsage=-1%2C-2%2C-3%2C-4%2C-5%2C-6%2C-7&allocate=***&backorder=***&remerge=***&check=*n&addressId=${addressID}&requesttype=ajax"))
          .check(regex("""orderId": \["(\d+)""").saveAs("orderId"))
          .check(bodyString.saveAs("fullBody"))
          //.check(regex("""orderItemId": \["(\d+)""").saveAs("orderItemId"))
          .check(status.is(200))
        )

        .exec(http("OrderShippingBillingCmd")
          .get(getbasehttps() + "/webapp/wcs/stores/servlet/OrderShippingBillingCmd?orderId=${orderId}&langId=-24&storeId=${StoreID}&catalogId=${catalogID}&showRegTag=T")
          .check(regex("""GrandOrderTotal" value='(.*?)'""").saveAs("total"))
          .check(status.is(200))
        )

        .exec(http("HBCPIRemoveCmd")
          .post(url4)
          .body(StringBody("url=HBCPIRemoveCmd&pitype=all&orderid=${orderId}&requesttype=ajax"))
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
          .body(StringBody("objectId=&requesttype=ajax"))
          .check(status.is(200))

        )

        .exec(http("GC1Amount")
          .post(url6)
          .body(StringBody("objectId=&requesttype=ajax"))
          .check(status.is(200))

        )

        .exec(http("GC1Amount")
          .post(url6)
          .body(StringBody("objectId=&requesttype=ajax"))
          .check(status.is(200))

        )
        .exec(http("AjaxTenderType")
          .post(url7)
          .body(StringBody("orderId=${orderId}&requesttype=ajax"))
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
          .queryParam("amount","undefined")
          .body(StringBody("objectId=&requesttype=ajax"))
          .check(status.is(200))

        )

        .exec(http("GC1Amount")
          .post(url6)
          .queryParam("piAmount","")
          .body(StringBody("objectId=&requesttype=ajax"))
          .check(status.is(200))

        )

        .exec(http("GC2Amount")
          .post(url8)
          .queryParam("piAmount","")
          .body(StringBody("objectId=&requesttype=ajax"))
          .check(status.is(200))

        )

        .exec(http("TraditionalAjaxShippingDetailsURL")
          .post(url9)
          .queryParam("storeId","${StoreID}")
          .queryParam("catalogId","${catalogID}")
          .queryParam("langId","-24")
          .body(StringBody("shipmentDetailsArea=update&objectId=&requesttype=ajax"))
          .check(status.is(200))

        )

        .exec(http("CurrentOrderInformationView")
          .post(url10)
          .queryParam("storeId","${StoreID}")
          .queryParam("catalogId","${catalogID}")
          .queryParam("langId","-24")
          .queryParam("orderId","")
          .queryParam("isFromPayment","true")
          .body(StringBody("objectId=&requesttype=ajax"))
          .check(status.is(200))

        )


        .exec(http("CreditAmount_3")
          .post(url5)
          .queryParam("piAmount","${total}")
          .queryParam("catalogId","${catalogID}")
          .queryParam("langId","-24")
          .queryParam("CurrencySymbolToFormat","$")
          .queryParam("paymentAreaNumber","1")
          .queryParam("storeId","${StoreID}")
          .queryParam("amount","${total}")
          .body(StringBody("objectId=&requesttype=ajax"))
          .check(status.is(200))

        )

        .exec(http("CreditAmount_4")
          .post(url5)
          .queryParam("piAmount","${total}")
          .queryParam("catalogId","${catalogID}")
          .queryParam("langId","-24")
          .queryParam("CurrencySymbolToFormat","$")
          .queryParam("paymentAreaNumber","1")
          .queryParam("storeId","${StoreID}")
          .queryParam("amount","${total}")
          .body(StringBody("objectId=&requesttype=ajax"))
          .check(status.is(200))

        )


        .exec(session => {

          insertGroupResponseTimeToInfluxDB(session toString, "Shipping")
          insertGroupResponseTimeToNewRelic(session toString, "Shipping")
          session

        })

  }

  }
