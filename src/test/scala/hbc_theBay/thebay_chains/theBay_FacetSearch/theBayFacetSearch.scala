package hbc_theBay.thebay_chains.theBay_FacetSearch

import hbc_theBay.theBay_base.theBay
import io.gatling.core.Predef._
import io.gatling.http.Predef._

/**
  * Created by aindana on 3/14/2016.
  */
object theBayFacetSearch extends theBay{
  lazy val url = getbasehttp() + "/webapp/wcs/stores/servlet/AjaxCatalogSearchResultView"
  def TheBayFacetSearch = group("FacetSearch_Transaction") {
      exec(http("TheBayFacetSearch")
        .post(url)
        .queryParam("searchTermScope","")
        .queryParam("ascBrandFlag","false")
        .queryParam("searchType","1000")
        .queryParam("filterTerm","")
        .queryParam("orderBy","${orderByVal}")
        .queryParam("maxPrice","")
        .queryParam("top_category","${top_category}")
        .queryParam("showResultsPage","true")
        .queryParam("langId","-24")
        .queryParam("beginIndex","0")
        .queryParam("sType","SimpleSearch")
        .queryParam("metaData","")
        .queryParam("manufacturer","")
        .queryParam("filterSelected","")
        .queryParam("resultCatEntryType","")
        .queryParam("catalogId","${catalogID}")
        .queryParam("searchTerm","")
        .queryParam("facet","")
        .queryParam("minPrice","")
        .queryParam("categoryId","${categoryId}")
        .queryParam("storeId","${StoreID}")
        .queryParam("facetLabel","")
        .queryParam("ascBrandFlag","false")
        .formParam("searchResultsPageNum","${searchResultsPageNumVal}")
        .formParam("searchResultsView","")
        .formParam("searchResultsURL","//www.thebay.com/webapp/wcs/stores/servlet/AjaxCatalogSearchResultView?searchTermScope=&ascBrandFlag=false&searchType=1000&filterTerm=&orderBy=${orderByVal}&maxPrice=&top_category=${top_category}&showResultsPage=true&langId=-24&beginIndex=0&sType=SimpleSearch&metaData=&manufacturer=&filterSelected=&resultCatEntryType=&catalogId=${catalogID}&pageView=image&searchTerm=&facet=&minPrice=&categoryId=${categoryId}&storeId=${StoreID}&facetLabel=")
        .formParam("objectId","")
        .formParam("requesttype","ajax")
        .check(status.is(200))
      )

        .exec(session => {

          insertGroupResponseTimeToInfluxDB(session toString, "FacetSearch")
          insertGroupResponseTimeToNewRelic(session toString, "FacetSearch")
          session

        })


    }

  }
