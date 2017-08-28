package hbc_saksO5Mobile.saksO5m_base

import hbc_loadTests.LoadTest
import io.gatling.core.Predef._

import scalaj.http.Http

/**
  * Created by aindana on 10/31/2016.
  */
class SaksO5Mobile extends LoadTest with saksO5mSettings with saksO5mWLM{

  def insertGroupResponseTimeToInfluxDB(session:String, transaction:String) = InfluxDB("SaksMobile")_

  def insertGroupResponseTimeToNewRelic(session:String, transaction:String) = NewRelic("SaksMobile")_

}
