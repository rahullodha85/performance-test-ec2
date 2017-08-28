package hbc_newLT.newLT_chains.newLT_PARefinements

import java.util.Random


import hbc_newLT.newLT_base.newLT
import io.gatling.core.Predef._
import io.gatling.http.Predef._

/**
  * Created by aindana on 3/14/2016.
  */
object newLTFilterBy extends newLT{

  def FilterBy = group("FilterBy_Transaction") {
    exec(session => {
      val rand = new Random(System currentTimeMillis)
      val cat = session("folder_id").as[String] substring(7)
      val sort1 = "P_" + cat + "_sort"
      val sort2 = "P_arrivaldate|1||" + sort1 + "||P_brandname||P_product_code"
      session.set("Ns0","P_saleprice")
        .set("Ns1","P_brandname||P_product_code")
        .set("Ns2",sort1)
        .set("Ns3","P_bestsellers_units|1||P_brandname||P_product_code")
        .set("Ns4","P_review_score|1||P_review_count|1||P_brandname||P_product_code")
        .set("Ns5",sort2)
        .set("Ns6","P_sale_flag|1")


    })

      .exec(http("FilterBy")
        .get(getbasehttp() + "${PALink}")
        .queryParam("FOLDER<>folder_id","${folder_id}")
        .queryParam("Ns","${Ns"+ scala.util.Random.nextInt(6) + "}")
        .check(status.is(200))

      )

      .exec(session => {

        insertGroupResponseTimeToInfluxDB(session toString, "FilterBy")
        insertGroupResponseTimeToNewRelic(session toString, "FilterBy")
        session

      })

    }


  }
