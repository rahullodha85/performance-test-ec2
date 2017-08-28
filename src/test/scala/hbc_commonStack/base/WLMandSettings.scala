package hbc_commonStack.base

import com.typesafe.config.ConfigFactory
import io.gatling.core.Predef._

/**
  * Created by aindana on 11/1/2016.
  */

trait DataSettings {

  val BannerVal = ConfigFactory.load("data/Config/Banner").getString("Banner")

  lazy val SearchString = if(BannerVal.equalsIgnoreCase("s5a")) jsonFile("src/test/resources/data/hbc_saks/SearchString.json").random
  else if(BannerVal.equalsIgnoreCase("o5a")) jsonFile("src/test/resources/data/hbc_saksO5/SearchString.json").random
  else jsonFile("src/test/resources/data/hbc_saks/SearchString.json").random

  lazy val BillingDetails = if(BannerVal.equalsIgnoreCase("s5a")) jsonFile("src/test/resources/data/hbc_saks/BillingDetails.json").circular
  else if(BannerVal.equalsIgnoreCase("o5a")) jsonFile("src/test/resources/data/hbc_saksO5/BillingDetails.json").circular
  else jsonFile("src/test/resources/data/hbc_saks/BillingDetails.json").circular

  lazy val ShippingDetails = if(BannerVal.equalsIgnoreCase("s5a")) jsonFile("src/test/resources/data/hbc_saks/ShippingDetails.json").circular
  else if(BannerVal.equalsIgnoreCase("o5a")) jsonFile("src/test/resources/data/hbc_saksO5/ShippingDetails.json").circular
  else jsonFile("src/test/resources/data/hbc_saks/ShippingDetails.json").circular

  lazy val Products = if(BannerVal.equalsIgnoreCase("s5a")) jsonFile("src/test/resources/data/hbc_saks/Products.json").random
  else if(BannerVal.equalsIgnoreCase("o5a")) jsonFile("src/test/resources/data/hbc_saksO5/Products.json").random
  else jsonFile("src/test/resources/data/hbc_saks/Products.json").random

  lazy val changeQty = if(BannerVal.equalsIgnoreCase("s5a")) jsonFile("src/test/resources/data/hbc_saks/ChangeQty.json").circular
  else if(BannerVal.equalsIgnoreCase("o5a")) jsonFile("src/test/resources/data/hbc_saksO5/ChangeQty.json").circular
  else jsonFile("src/test/resources/data/hbc_saks/ChangeQty.json").circular

  lazy val userInfo =  jsonFile("src/test/resources/data/hbc_saksO5/UserInformation.json").circular

}

trait LoadSettings {

  val nothing: Int = System.getProperty("Nothing").toInt
  val duration: Int = System.getProperty("Duration").toInt
  val rampUp: Int = System.getProperty("RampUp").toInt

}

trait WLMSettings {

  val Banner = ConfigFactory.load("data/Config/Banner").getString("Banner")

  val HTTPSConfig = if(Banner.equalsIgnoreCase("s5a")) ConfigFactory.load("data/hbc_saks/HTTPS") else if(Banner.equalsIgnoreCase("o5a")) ConfigFactory.load("data/hbc_saksO5/HTTPS") else ConfigFactory.load("data/hbc_saks/HTTPS")

  var percent: Double = System.getProperty("Percent").toDouble/100
  lazy val ordersperhour: Int = System.getProperty("ordersperhour").toInt

  lazy val CSTransactionsPerHour: Int = System.getProperty("CSTransactionsPerHour").toInt
  lazy val PDPTransactionsPerHour: Int = System.getProperty("PDPTransactionsPerHour").toInt
 // val additionalTraffic: Double = 1.25
 // percent = percent * additionalTraffic

  val orderPercent: Double = percent * 1.1

  lazy val ordersPace = 120

  lazy val ordersUsers = (ordersperhour*ordersPace)/3600

  val config = if(Banner.equalsIgnoreCase("s5a")) ConfigFactory.load("data/hbc_saks/WLM") else if(Banner.equalsIgnoreCase("o5a")) ConfigFactory.load("data/hbc_saksO5/WLM") else ConfigFactory.load("data/hbc_saks/WLM")

  val guestRatio = 0.6
  val regRatio = 0.4
  val order1Ratio = 0.4
  val order2Ratio = 0.3
  val order5Ratio = 0.1
  val order10Ratio = 0.1
  val order15Ratio = 0.1

  val HomePageT= config.getInt("HomePageT")*percent
  val TextSearchT = config.getInt("TextSearchT")*percent
  val PDPT = config.getInt("PDPT")*percent
  val ShoppingBagT = config.getInt("ShoppingBagT")*percent
  val BillingT= config.getInt("BillingT")*percent
  val OrderT = config.getInt("OrderT")*percent
  val PAMenuT = config.getInt("PAMenuT")*percent
  val PARefineT= config.getInt("PARefineT")*percent
  val PromoT = config.getInt("PromoT")*percent
  val cartAddT = config.getInt("cartAddT")*percent
  val cartRemT = config.getInt("cartRemT")*percent

  val PARefineCounter = 1

  val HomePagePace = 10
  val TopNavPace = 30
  val PARefinePace = 30
  val SearchTermPace = 30
  val PDPPace = 20
  val AddToBagPace = 20
  val RemoveItemPace = 5
  val BillingPace = 30

  val CSServicePace = 30
  val PDPServicePace = 30

  lazy val CSServiceUsers = (CSTransactionsPerHour*CSServicePace)/3600
  lazy val PDPServiceUsers = (PDPTransactionsPerHour*PDPServicePace)/3600

  val SearchTermUsers = (TextSearchT*SearchTermPace)/3600
  val HomePageUsers = ((HomePageT-PAMenuT)*HomePagePace)/3600
  val TopNavUsers = (PAMenuT*(HomePagePace + TopNavPace + PARefineCounter*PARefinePace))/3600
  val orderT_1Users = ((OrderT * order1Ratio)*(PDPPace + AddToBagPace + BillingPace))/3600
  val orderT_2Users = ((OrderT * order2Ratio)*((PDPPace + AddToBagPace)*2 + BillingPace))/3600
  val orderT_5Users = ((OrderT * order5Ratio)*((PDPPace + AddToBagPace)*5 + BillingPace))/3600
  val orderT_10Users = ((OrderT * order10Ratio)*((PDPPace + AddToBagPace)*10 + BillingPace))/3600
  val orderT_15Users = ((OrderT * order15Ratio)*((PDPPace + AddToBagPace)*15 + BillingPace))/3600
  val BillingUsers = ((BillingT - OrderT)*(PDPPace + AddToBagPace + BillingPace))/3600
  val AddToBagTran = (cartAddT-cartRemT) - (OrderT * 0.4 + OrderT * 0.3 * 2 + OrderT * 0.1 * 5 + OrderT * 0.1 * 10 + OrderT * 0.1 * 15 + (BillingT - OrderT))
  val AddToBagUsers = (AddToBagTran * (PDPPace + AddToBagPace))/3600
  val PDPTran = PDPT - (OrderT * 0.4 + OrderT * 0.3 * 2 + OrderT * 0.1 * 5 + OrderT * 0.1 * 10 + OrderT * 0.1 * 15 + (BillingT - OrderT) + AddToBagTran)
  val PDPUsers = (PDPTran * PDPPace)/3600
  val RemoveItemUsers = cartRemT * (PDPPace + AddToBagPace + RemoveItemPace)/3600


  // HTTPS and HTTP Toggle

  lazy val HomePage_HTTPS = HTTPSConfig.getString("HomePage_HTTPS")
  lazy val PA_HTTPS = HTTPSConfig.getString("PA_HTTPS")
  lazy val PDP_HTTPS = HTTPSConfig.getString("PDP_HTTPS")
  lazy val Search_HTTPS = HTTPSConfig.getString("Search_HTTPS")

}