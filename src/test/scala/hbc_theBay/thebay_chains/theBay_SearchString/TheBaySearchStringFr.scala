package hbc_theBay.thebay_chains.theBay_SearchString

import BaseTest.BaseTest

import io.gatling.core.Predef._
import io.gatling.http.Predef._

/**
  * Created by aindana on 3/14/2016.
  */
object TheBaySearchStringFr extends BaseTest{

  val SearchStringFr = jsonFile("src/test/resources/data/SearchStringFr.json").random


  def TheBaySearchStringFr = group("SearchString_Transaction") {
    feed(SearchStringFr)
      .exec(http("Product Array")
        .get(baseUrlHttp + "/webapp/wcs/stores/servlet/fr/SearchDisplay?storeId=10701&catalogId=10652&langId=-25&pageSize=12&beginIndex=0&sType=SimpleSearch&resultCatEntryType=2&showResultsPage=true&pageView=image&searchSource=Q&searchTerm=${SearchString}&x=0&y=0")
        .check(regex("""\t\t\t"(\S+)",""").findAll.saveAs("ProductIDAll"))
        .check(status.is(200))

      )






  }

}