package hbc_saksO5Mobile.saksO5m_chains.saksO5m_checkout

import BaseTest.BaseTest
import hbc_saksO5Mobile.saksO5m_base.SaksO5Mobile
import io.gatling.core.Predef._
import io.gatling.http.Predef._

/**
  * Created by aindana on 3/14/2016.
  */
object SaksO5MobileTextSearch extends SaksO5Mobile {

  def TextSearch = group("TextSearch_Transaction") {
    feed(SearchString)
        .exec(http("TextSearch")
        .get(getbasehttp() + "/eSearch.jsp?SearchString=&EndecaSearchButton=&N_Dim=0&Ntt=${SearchString}&Ns=&N=0&Ntk=Entire+Site&Ntx=mode%2Bmatchpartialmax&prp8=t15&prp13=&PA=TRUE")
        .check(status.is(200))
      )

        .exec(session => {
          insertGroupResponseTimeToInfluxDB(session toString, "TextSearch")
          insertGroupResponseTimeToNewRelic(session toString, "TextSearch")
          session

        })


  }
}