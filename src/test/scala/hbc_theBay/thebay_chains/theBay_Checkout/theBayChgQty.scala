package hbc_theBay.thebay_chains.theBay_Checkout

import hbc_theBay.theBay_base.theBay
import io.gatling.core.Predef._
import io.gatling.http.Predef._

/**
  * Created by aindana on 3/14/2016.
  */
object theBayChgQty extends theBay{
  lazy val url1 =  getbasehttps() + "/webapp/wcs/stores/servlet/AjaxOrderChangeServiceItemUpdate"
  lazy val url2 =  getbasehttps() + "/webapp/wcs/stores/servlet/ShopCartDisplayView"
  def ChgQty = group("ChangeQtyt_Transaction") {
      exec(http("AjaxOrderChangeServiceItemUpdate")
        .post(url1)
        .body(StringBody("orderId=.&storeId=${StoreID}&catalogId=${catalogID}&langId=-24&calculationUsage=-1&orderItemId_1=${orderItemId}&quantity_1=1&requesttype=ajax"))
        //.check(bodyString.saveAs("test"))
        .check(status.is(200))
            )

      .exec(http("ShopCartDisplayView")
      .post(url2)
        .queryParam("shipmentType","single")
        .queryParam("storeId","${StoreID}")
        .queryParam("catalogId","${catalogID}")
        .queryParam("orderId","${orderId}")
        .queryParam("langId","-24")
        .body(StringBody("objectId=&requesttype=ajax"))
          .check(regex("""ShopCartDisplay.jsp""").exists)
        .check(status.is(200))
      //.check(bodyString.saveAs("test"))

    )

        .exec(session => {

          insertGroupResponseTimeToInfluxDB(session toString, "ChangeQty")
          insertGroupResponseTimeToNewRelic(session toString, "ChangeQty")
          session

        })

  }

  }
