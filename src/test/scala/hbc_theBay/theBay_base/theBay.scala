package hbc_theBay.theBay_base

import hbc_loadTests.LoadTest


/**
  * Created by aindana on 10/31/2016.
  */
class theBay extends LoadTest with theBaySettings with theBayWLM{

  def insertGroupResponseTimeToInfluxDB(session:String, transaction:String) = InfluxDB("theBay_")(session:String, transaction:String)

  def insertGroupResponseTimeToNewRelic(session:String, transaction:String) = NewRelic("theBay_")(session:String, transaction:String)


}
