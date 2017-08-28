package hbc_theBay.thebay_chains.theBay_SearchString

import java.util.Random

import BaseTest.BaseTest
import io.gatling.core.Predef._
import io.gatling.http.Predef._

/**
  * Created by aindana on 3/14/2016.
  */
object TheBaySearchStringWrong extends BaseTest{

  lazy val TheBaySearchStringWrong = group("SearchString_Transaction") {
    exec(session => {
      val r = new scala.util.Random
      val sb = new StringBuilder
      for (i <- 1 to 6) sb append(r nextPrintableChar)
      session.set("SearchString",sb toString)
    })

      .exec(http("Product Array")
        .get(baseUrlHttp + "/webapp/wcs/stores/servlet/en/SearchDisplay?storeId=${StoreID}&catalogId=${catalogID}&langId=-1&pageSize=12&beginIndex=0&sType=SimpleSearch&resultCatEntryType=2&showResultsPage=true&pageView=image&searchSource=Q&searchTerm=${SearchString}&x=0&y=0")
        .check(status.in(404,400,200))

      )




      .exec(session => {

        insertGroupResponseTime(session toString, "BAY_SearchString")
        session

      })



  }

}