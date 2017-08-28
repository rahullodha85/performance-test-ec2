package hbc_newLT.newLT_base

import hbc_loadTests.LoadTest


/**
  * Created by aindana on 10/31/2016.
  */
class newLT extends LoadTest with newLTSettings with newLTWLM{

  def insertGroupResponseTimeToInfluxDB(session:String, transaction:String) = InfluxDB("newLT_")(session:String, transaction:String)

  def insertGroupResponseTimeToNewRelic(session:String, transaction:String) = NewRelic("newLT_")(session:String, transaction:String)


}
