package hbc_theBay.thebay_chains.theBay_SearchString

import java.util.Random

import hbc_theBay.theBay_base.theBay
import io.gatling.core.Predef._
import io.gatling.http.Predef._

/**
  * Created by aindana on 3/14/2016.
  */
object theBaySearchString extends theBay{
    def Search_String = group("SearchString_Transaction") {
    feed(SearchString)
      .exec(http("Product Array")
        .get(getbasehttp() + "/webapp/wcs/stores/servlet/en/SearchDisplay?storeId=10701&catalogId=10652&langId=-1&pageSize=12&beginIndex=0&sType=SimpleSearch&resultCatEntryType=2&showResultsPage=true&pageView=image&searchSource=Q&searchTerm=${SearchString}&x=0&y=0")
        .check(regex("""\t\t\t"(\S+)",""").findAll.saveAs("ProductIDAll"))
        .check(regex("""orderBy=(\d+)""").findAll.saveAs("orderBy"))
        .check(regex("""searchResultsPageNum']='(\d+)""").findAll.saveAs("searchResultsPageNum"))
        .check(regex("""top_category=(\d+)""").find.saveAs("top_category"))
        .check(regex("""categoryId=(\d+)""").find.saveAs("categoryId"))
        .check(status.is(200))

      )


      .exec(session => {
        val rand = new Random(System currentTimeMillis);
        val ProductIDList: List[String] = session("ProductIDAll").as[List[String]]
        val random_index = rand nextInt(ProductIDList.length-1) + 1;
        val result = ProductIDList(random_index);
        val orderBy: List[String] = session("orderBy").as[List[String]]
        val random_index2 = rand nextInt(orderBy length);
        val orderBY = orderBy(random_index2);
        val searchResultsPageNum: List[String] = session("searchResultsPageNum").as[List[String]]
        val mapp = (orderBy zip searchResultsPageNum).toMap
        
       /*println(result)
          println(session("StoreID").as[String])
          println(session("catalogID").as[String])*/
        session.set("ProductID",result).set("orderByVal",orderBY).set("searchResultsPageNumVal",mapp(orderBY))

      })


      .exec(session => {

        insertGroupResponseTimeToInfluxDB(session toString, "SearchString")
        insertGroupResponseTimeToNewRelic(session toString, "SearchString")
        session

      })



  }

}
