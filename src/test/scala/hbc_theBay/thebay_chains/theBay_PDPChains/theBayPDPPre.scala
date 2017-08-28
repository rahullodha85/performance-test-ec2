package hbc_theBay.thebay_chains.theBay_PDPChains

import java.util.Random

import BaseTest.BaseTest
import hbc_theBay.theBay_base.theBay
import io.gatling.core.Predef._
import io.gatling.http.Predef._

/**
  * Created by aindana on 3/14/2016.
  */
object theBayPDPPre extends theBay{

  def PDPPre = group("Pre_Transaction") {
    exec(http("Get Index")
    //  AllItems
      .get(getbasehttp() + "/webapp/wcs/stores/servlet/en/SearchDisplay?storeId=10701&catalogId=10652&langId=-24&pageSize=12&beginIndex=0&sType=SimpleSearch&resultCatEntryType=2&showResultsPage=true&pageView=image&searchSource=Q&searchTerm=&x=0&y=0")
    // .get(baseUrlHttp + "/webapp/wcs/stores/servlet/en/SearchDisplay?&searchType=1000&showResultsPage=true&langId=-24&beginIndex=0&sType=SimpleSearch&metaData=&pageSize=&catalogId=10652&pageView=image&facet=ads_f6_ntk_cs:Y&categoryId=&storeId=10701&facetLabel=SALE&rremail=rochelle.ezekiel@hbc.com&email=rochelle.ezekiel@hbc.com&site_refer=EMLHB6436DAILY&om_rid=Nso3-p&om_mid=_BYMiPsB9VEXued")
      .check(regex("""var totalCount = (\d+)""").find.saveAs("TotalProducts"))
      .check(status.is(200))

    )



      .exec(session => {
        val rand = new Random(System currentTimeMillis);
        val randIndex = rand nextInt(session("TotalProducts").as[String].toInt / 100)
        session.set("randIndex", rand nextInt(session("TotalProducts").as[String].toInt / 100))

      })


      .exec(http("PDP Item Pick")
        .get(getbasehttp() + "/webapp/wcs/stores/servlet/en/SearchDisplay?storeId=10701&catalogId=10652&langId=-24&pageSize=12&beginIndex=${randIndex}00&sType=SimpleSearch&resultCatEntryType=2&showResultsPage=true&pageView=image&searchSource=Q&searchTerm=&x=0&y=0")
        .check(regex("""\t\t\t"(\S+)",""").findAll.saveAs("ProductIDAll"))
        .check(regex("""content="storeId_(\d+)""").find.saveAs("StoreID"))
        .check(regex("""setCommonParameters\('-\d+','\d+','(\d+)""").find.saveAs("catalogID"))
        .check(regex("""orderBy=(\d+)""").findAll.saveAs("orderBy"))
        .check(regex("""searchResultsPageNum']='(\d+)""").findAll.saveAs("searchResultsPageNum"))
        .check(regex("""top_category=(\d+)""").find.saveAs("top_category"))
        .check(regex("""categoryId=(\d+)""").find.saveAs("categoryId"))
        .check(status.is(200))

      )

      .exec(session => {
        val rand = new Random(System currentTimeMillis);

        val ProductIDList: List[String] = session("ProductIDAll").as[List[String]]
        val random_index = rand nextInt(ProductIDList.length - 1) + 1;
        val result = ProductIDList(random_index);
        val orderBy: List[String] = session("orderBy").as[List[String]]
        val random_index2 = rand nextInt(orderBy length);
        val orderBY = orderBy(random_index2);
        val searchResultsPageNum: List[String] = session("searchResultsPageNum").as[List[String]]
        val mapp = (orderBy zip searchResultsPageNum).toMap
        /*  println(result)
          println(session("StoreID").as[String])
          println(session("catalogID").as[String])*/
        session.set("ProductID", result).set("orderByVal", orderBY).set("searchResultsPageNumVal", mapp(orderBY))

      })

    }

  }
