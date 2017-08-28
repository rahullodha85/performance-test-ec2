package hbc_newLT.newLT_chains.newLT_PARefinements

import java.util.Random

import hbc_newLT.newLT_base.newLT
import io.gatling.core.Predef._
import io.gatling.http.Predef._

/**
  * Created by aindana on 3/14/2016.
  */
object newLTPARefine extends newLT{

  def PARefine = group("PARefine_Transaction") {
    exec(session => {
      val PARefineList = session("PARefineList").as[List[String]]
      val rand = new Random(System currentTimeMillis)

      val random_index = rand nextInt (PARefineList length)
      var result = PARefineList(random_index)
      val urlToBeConverted = session("baseRefinementUrl").as[String] + "&N=" + session("nonRefinementsParameter").as[String] + "+" + result
    //  println(urlToBeConverted)
    //  println(session("PALink").as[String])
      session.set("urlToBeConverted",urlToBeConverted)
    })

        .exec(http("PARefine_Include")
        .get(getbasehttp()+"/include/productarray/update_refinements.jsp")
        .queryParam("urlToBeConverted","${urlToBeConverted}")
        .queryParam("currentURL","${PALink}")
         .check(bodyString.saveAs("newPALink"))
        .check(status.is(200))

      )

      .exec(http("PARefine_2")
        .get(getbasehttp()+"${newPALink}")
        .check(status.is(200))

      )

      .exec(session => {

        insertGroupResponseTimeToInfluxDB(session toString, "PARefine")
        insertGroupResponseTimeToNewRelic(session toString, "PARefine")
        session

      })

    }


  }
