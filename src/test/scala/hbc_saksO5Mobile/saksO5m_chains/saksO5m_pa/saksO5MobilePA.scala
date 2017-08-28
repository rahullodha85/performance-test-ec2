package hbc_saksO5Mobile.saksO5m_chains.saksO5m_checkout

import java.util.Random

import BaseTest.BaseTest
import hbc_saksO5Mobile.saksO5m_base.SaksO5Mobile
import io.gatling.core.Predef._
import io.gatling.http.Predef._

/**
  * Created by aindana on 3/14/2016.
  */
object saksO5MobilePA extends SaksO5Mobile {

  def PA(PALink: String = "None") = group("ProductArray_Transaction") {
    exec(session => {
      if(!PALink.equals("None"))
        {
          session.set("PALink",PALink)
        }
      session
    })

        .exec(http("eSearch")
        .get(getbasehttp() + "${PALink}")
        .check(regex("""li id="(\d+)"""").findAll.saveAs("ProductIDAll"))
        .check(regex("""folder_id=(\d+)""").saveAs("folder_id"))
        )


        .exec(session => {
      //    println(session("PALink").as[String])
          insertGroupResponseTimeToInfluxDB(session toString, "TopNav")
          insertGroupResponseTimeToNewRelic(session toString, "TopNav")
          session

        })
  }


}