package hbc_saksO5Mobile.saksO5m_chains.saksO5m_checkout

import java.util.Random

import hbc_saksO5Mobile.saksO5m_base.SaksO5Mobile
import io.gatling.core.Predef._
import io.gatling.http.Predef._

/**
  * Created by aindana on 3/14/2016.
  */
object SaksO5MobileFacetSearch extends SaksO5Mobile{

  def FacetSearch = group("PARefine_Transaction") {
    exec(session => {

     var PAlink = session("PALink").as[String]
   // println(PAlink)
      session

    })

      .exec(session => {
      val rand = new Random(System currentTimeMillis)
      val cat = session("folder_id").as[String] substring 7
      val sort1 = "P_" + cat + "_sort"
      val sort2 = "P_arrivaldate|1||" + sort1 + "||P_brandname||P_product_code"
      session.set("Ns0","P_saleprice")
        .set("Ns1","P_brandname||P_product_code")
        .set("Ns2",sort1)
        .set("Ns3","P_bestsellers_units|1||P_brandname||P_product_code")
        .set("Ns4","P_review_score|1||P_review_count|1||P_brandname||P_product_code")
        .set("Ns5",sort2)
        .set("Ns6","P_sale_flag|1")
          .set("cat",cat)


    })

      .exec(http("Filter By")
      .get(getbasehttp() + "/eSearch.jsp")
        .queryParam("N_Dim","0")
        .queryParam("PA","TRUE")
        .queryParam("bmSingle","N_Dim")
        .queryParam("Ns","${Ns"+ scala.util.Random.nextInt(6) + "}")
        .queryParam("N","${cat}")
        .check(status.is(200))
    )

      .exec(session => {

        insertGroupResponseTimeToInfluxDB(session toString, "FacetSearch")
        insertGroupResponseTimeToNewRelic(session toString, "FacetSearch")
        session

      })

    }


  }
