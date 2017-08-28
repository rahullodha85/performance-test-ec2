package hbc_saksO5Mobile.saksO5m_chains.saksO5m_checkout

import java.util.{NoSuchElementException, Random}

import BaseTest.BaseTest
import hbc_saksO5Mobile.saksO5m_base.SaksO5Mobile
import io.gatling.core.Predef._
import io.gatling.http.Predef._

/**
  * Created by aindana on 3/14/2016.
  */
object SaksO5MobilePDP extends SaksO5Mobile {


  //  val uri3 = "http://www.saksfifthavenue.com/favicon.ico"

  def PDP = group("PDP_Transaction") {
      exec(http("PDP")
        .get("${PDPLink}")
        .check(regex("""prd_id=(\d+)""").saveAs("prd_id"))
      //  .check(regex("""folder_id=(\d+)""").saveAs("folder_id"))
        .check(regex("""This item is sold out. Add to Wait List to be notified when it is back in stock.</h""").count.saveAs("SoldOut"))
        .check(regex("""sku_id":"(\d+)","price":\{.*?\},"status_alias":"(\w+)""").count.saveAs("sku_idCount"))
        .check(regex("""sku_id":"(\d+)","price":\{.*?\},"status_alias":"\w+""").findAll.saveAs("sku_id_values"))
        .check(regex("""sku_id":"\d+","price":\{.*?\},"status_alias":"(\w+)""").findAll.saveAs("status"))
        //.check(regex("""<h4 class="product-overview__product-code" data-reactid="\S+">(\d+)<""").saveAs("ProductID"))
        .check(regex("""products":\[\{"brand":".*","id":"(\d+)"""").saveAs("ProductID"))
        .check(status.is(200))
     //   .check(bodyString.saveAs("ProdArray"))

      )

        .exec(session => {
          try {
            val mapp = (session("status").as[List[String]] zip session("sku_id_values").as[List[String]]).toMap
            session.set("sku_id", mapp("available")).set("availableItems", 1)
          }
          catch{
            case e:NoSuchElementException => session.set("availableItems", 0)
          }

        })

        .exec(session => {
         // println(session("ProductID").as[String])
          insertGroupResponseTimeToInfluxDB(session toString, "PDP")
          insertGroupResponseTimeToNewRelic(session toString, "PDP")
          session

        })
  }


}
