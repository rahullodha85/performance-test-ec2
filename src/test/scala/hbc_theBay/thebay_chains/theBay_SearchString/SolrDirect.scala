package hbc_theBay.thebay_chains.theBay_SearchString

import BaseTest.BaseTest
import io.gatling.core.Predef._
import io.gatling.http.Predef._

/**
  * Created by aindana on 3/14/2016.
  */
object SolrDirect extends BaseTest{

  val SearchStringFr = jsonFile("src/test/resources/data/SearchStringFr.json").random
 // val url = System.getProperty("Url").replaceAll("\"","")
  val SolrLink = jsonFile("src/test/resources/data/SolrLink.json").random

  def SolrDirect = group("SolrDirect_Transaction") {
    feed(SearchStringFr)
       .feed(SolrLink)
      .exec(http("Product Array")
          .get("${SolrLink}")
          .queryParam("q","${SearchString}")
          //  .check(bodyString.saveAs("FullResponse"))
          .check(regex("""numFound":(\d+)""").saveAs("numFound"))
          .check(regex("""QTime":(\d+)""").saveAs("QTime"))
        .check(status.is(200))
      )

        .exec(session => {
       //   println(session("FullResponse").as[String])
          println("Search String: "  + session("SearchString").as[String])
          println("numFound: "  + session("numFound").as[String])
          println("QTime: "  + session("QTime").as[String])
          session
        })






  }

}