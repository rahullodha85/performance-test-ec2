package hbc_commonStack.base

import com.typesafe.config.ConfigFactory
import hbc_loadTests.LoadTest


/**
  * Created by aindana on 10/31/2016.
  */
class commonStack extends LoadTest with DataSettings with WLMSettings{

  val BannerInfo = ConfigFactory.load("data/Config/Banner").getString("Banner")

  def insertGroupResponseTimeToInfluxDB(session:String, transaction:String) = InfluxDB(BannerInfo+"_")(session:String, transaction:String)

  def insertGroupResponseTimeToNewRelic(session:String, transaction:String) = NewRelic(BannerInfo+"_")(session:String, transaction:String)

}
