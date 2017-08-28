package hbc_saksO5Mobile.saksO5m_chains.saksO5m_checkout

import java.util.Random

import BaseTest.BaseTest
import hbc_saksO5Mobile.saksO5m_base.SaksO5Mobile
import io.gatling.core.Predef._
import io.gatling.http.Predef._

/**
  * Created by aindana on 3/14/2016.
  */
object SaksO5MobileHomePage extends SaksO5Mobile{


  def HomePage = group("HomePage_Transaction") {
    exec(http ("request_0")
      .get(getbasehttp() + "/")
     )
// Capture all the mobile PA navigation Links
      .exec (http("mindex")
        .get (getbasehttp() + "/mindex.jsp")
        .check(regex("""mobile_url":"(\/\w*.?\w*.?N?=?\d+.\d+&pa=1&\w+=\w+)""").findAll.saveAs("PALinks"))
        .check(regex("""mobile_url":"(\/\w*.?\w*.?N?=?\d+.\d+&pa=1&\w+=\w+)""").count.saveAs("PALinksCount"))
      )
// Pick Up a random link
      .exec(session => {
        val rand = new Random(System currentTimeMillis);
        val PALinksList: List[String] = session("PALinks").as[List[String]]
        session.set("PALink",PALinksList(rand nextInt(PALinksList length)))

      })
//Insert response time data into influxDB and newRelic for realtime graphs
      .exec(session => {
        insertGroupResponseTimeToInfluxDB(session toString, "HomePage")
        insertGroupResponseTimeToNewRelic(session toString, "HomePage")
        session

      })

    }


  }
