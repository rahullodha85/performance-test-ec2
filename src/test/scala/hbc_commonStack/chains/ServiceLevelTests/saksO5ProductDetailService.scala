package hbc_commonStack.chains.ServiceLevelTests

import java.util.Random

import hbc_commonStack.base.commonStack
import io.gatling.core.Predef._
import io.gatling.http.Predef._

/**
  * Created by aindana on 3/14/2016.
  */
object saksO5ProductDetailService extends commonStack{

  def PDPService = group("PDPService_Transaction") {
    feed(Products)
      .exec(http("PDP")
      .get(getbasehttp() + "/v2/product-detail-service/pdp/product/${ProductID}")
        .check(status.is(200))
        .check(regex("""add_saks_suggests_item_service_product_array""").exists)
   //     .check(bodyString.saveAs("Response"))
    )

 /*     .exec(session => {
        println(session("Response").as[String])
        session
      })*/

      .exec(session => {
     // println("Test")
    /*  if(session("AddCount").as[Int]==0)
        {
          println("SESSION")
          println(session("Response").as[String])
        }*/
      insertGroupResponseTimeToInfluxDB(session toString, "ProductDetailService")
      insertGroupResponseTimeToNewRelic(session toString, "ProductDetailService")
      session

    })
  }


  }
