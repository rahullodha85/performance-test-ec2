package hbc_theBay.thebay_chains.theBay_Checkout

import hbc_theBay.theBay_base.theBay
import io.gatling.core.Predef._
import io.gatling.http.Predef._

/**
  * Created by aindana on 3/14/2016.
  */
object theBayHomePage extends theBay{

  def HomePage = group("HomePage_Transaction") {
      exec(http("HomePage")
        .get(getbasehttp())
        .check(regex("""storeId = '(\d+)""").find.saveAs("StoreID"))
        .check(regex("""catalogId = '(\d+)""").find.saveAs("catalogID"))
        .check(status.is(200))
      )

        .exec(session => {

          insertGroupResponseTimeToInfluxDB(session toString, "HomePage")
          insertGroupResponseTimeToNewRelic(session toString, "HomePage")
          session

        })




    }

  }
