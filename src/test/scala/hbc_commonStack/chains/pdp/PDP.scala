package hbc_commonStack.chains.pdp

import java.util.NoSuchElementException

import hbc_commonStack.base.commonStack
import io.gatling.core.Predef._
import io.gatling.http.Predef._

/**
  * Created by aindana on 3/14/2016.
  */
object PDP extends commonStack {


  //  val uri3 = "http://www.saksfifthavenue.com/favicon.ico"

  lazy val baseHttpUrl = if(PDP_HTTPS) getbasehttps() else getbasehttp()

  def PDP = group("PDP_Transaction") {
    exec(flushCookieJar)
      .exec(session => {

      var PDPLink = session("PDPLink").as[String]
      if(!session("PDPLink").as[String].contains("http"))
      {
        PDPLink = baseHttpUrl + session("PDPLink").as[String]
      }

      session.set("PDPLink",PDPLink)

    })


        .exec(http("ProductDetail.jsp")
        .get("${PDPLink}")
        .check(regex("""prd_id=(\d+)""").saveAs("prd_id"))
      //  .check(regex("""folder_id=(\d+)""").saveAs("folder_id"))
        .check(regex("""This item is sold out. Add to Wait List to be notified when it is back in stock.</h""").count.saveAs("SoldOut"))
        .check(regex("""sku_id":"(\d+)","price":\{.*?\},"status_alias":"(\w+)""").count.saveAs("sku_idCount"))
        .check(regex("""sku_id":"(\d+)","price":\{.*?\},"status_alias":"\w+""").findAll.saveAs("sku_id_values"))
        .check(regex("""sku_id":"\d+","price":\{.*?\},"status_alias":"(\w+)""").findAll.saveAs("status"))
            .check(regex(""""skus":\[(.*?)\]""").findAll.saveAs("AllSkus"))
        .check(regex("""products":\[\{"brand":".*","id":"(\d+)"""").saveAs("ProductID"))
        .check(status.is(200))
      //  .check(bodyString.saveAs("ProdArray"))

      )
     /* .exec(session => {
      //  println(session("AllSkus").as[List[String]].last)
        val newList = session("sku_id_values").as[List[String]].filter(session("AllSkus").as[List[String]].last.contains(_))
        println(session("status").as[List[String]].take(newList.length) zip newList)
        session

      })*/

        .exec(session => {
          val newList = session("sku_id_values").as[List[String]].filter(session("AllSkus").as[List[String]].last.contains(_))
          try {
            val mapp = (session("status").as[List[String]].take(newList.length) zip newList).toMap
            println(mapp)
      //      val mapp = (session("status").as[List[String]] zip session("sku_id_values").as[List[String]]).toMap
           val sku_id = if(mapp isDefinedAt "available") mapp("available") else if(mapp isDefinedAt "preorder") mapp("preorder") else "0"
            println("SKU : " + sku_id)
            session.set("sku_id", sku_id).set("availableItems", 1)
          }
          catch{
            case e:NoSuchElementException => session.set("availableItems", 0)
          }

        })

        .exec(session => {
   //       println(session("ProdArray").as[String])
          insertGroupResponseTimeToInfluxDB(session toString, "PDP")
          insertGroupResponseTimeToNewRelic(session toString, "PDP")
          session

        })
  }


}
