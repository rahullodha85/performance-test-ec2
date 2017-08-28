package hbc_newLT.newLT_chains.newLT_checkout

import java.util.Random

import hbc_newLT.newLT_base.newLT
import io.gatling.core.Predef._
import io.gatling.http.Predef._

/**
  * Created by aindana on 3/14/2016.
  */
object newLTShoppingBag extends newLT{

  val shoppingBagUrl = getbasehttps() + "/checkout/SaksBag.jsp"

  def ShoppingBag = group("shoppingBag_Transaction") {
      exec(http("shoppingBag")
      .get(shoppingBagUrl)
        .queryParam("PRODUCT<>prd_id","${prd_id}")
        .queryParam("FOLDER<>folder_id","${folder_id}")
        .queryParam("bmUID","${bmUID}")
        .check(status.is(200))
     )

            .exec(http("shoppingBag1")
            .post(shoppingBagUrl)
            .body(StringBody("bmForm=initialize_saks_bag_service"))
                .check(bodyString.saveAs("fullBody"))
            .check(regex("""cartItemId":"(\d+)""").saveAs("cartItemID"))
            .check(regex("""cartItemId":"(\d+)""").findAll.saveAs("cartItemIDList"))
            .check(regex("""cartItemId":"(\d+)""").count.saveAs("cartItemIDCount"))
            .check(regex(""""estDeliveryDate":".*?","id":"(\d+)","messages""").findAll.saveAs("Shipping"))
            .check(status.is(200))
          )

        .exec(session => {

        //  println(session("fullBody").as[String])
          val rand = new Random(System.currentTimeMillis());
          val ShippingList = session("Shipping").as[List[String]]
          val random_index = (rand nextInt (ShippingList.length))
          session.set("ShippingMethod", ShippingList(random_index))

        })

        .exec(session => {
          insertGroupResponseTimeToInfluxDB(session toString, "ShoppingBag")
          insertGroupResponseTimeToNewRelic(session toString, "ShoppingBag")
          session

        })

    }



}
