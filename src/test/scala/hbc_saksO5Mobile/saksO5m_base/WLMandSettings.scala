package hbc_saksO5Mobile.saksO5m_base

import io.gatling.core.Predef._

/**
  * Created by aindana on 11/1/2016.
  */

trait saksO5mSettings {
  lazy val SearchString = jsonFile("src/test/resources/data/hbc_saksO5m/SearchString.json").random
  lazy val BillingDetails = jsonFile("src/test/resources/data/hbc_saksO5m/BillingDetails.json").circular
  lazy val ShippingDetails = jsonFile("src/test/resources/data/hbc_saksO5m/ShippingDetails.json").circular
}

trait saksO5mLoadSettings {

  val nothing: Int = System.getProperty("Nothing").toInt
  val duration: Int = System.getProperty("Duration").toInt
  val rampUp: Int = System.getProperty("RampUp").toInt

}

trait saksO5mWLM {
  var percent: Double = System.getProperty("Percent").toDouble/100

  val additionalTraffic: Double = 1.25
  percent = percent * additionalTraffic

  val orderPercent: Double = percent * 1.1

  val HomePageT = 13166 * percent
  val TextSearchT = 11290 * percent
  val PDPT = 49630 * percent
  val ShoppingBagT = 11182 * percent
  val BillingT = 1688 * orderPercent
  val OrderT = 1126 * orderPercent
  val PAMenuT = 10285 * percent
  val PARefineT = 27358 * percent
  val AccountT = 527 * percent

  val PARefineCounter = 3

  val HomePagePace = 10
  val TopNavPace = 10
  val PARefinePace = 10
  val SearchTermPace = 10
  val PDPPace = 20
  val AddToBagPace = 20
  val BillingPace = 100

  val SearchTermUsers = (TextSearchT*SearchTermPace)/3600
  val HomePageUsers = ((HomePageT-PAMenuT)*HomePagePace)/3600
  val TopNavUsers = (PAMenuT*(HomePagePace + TopNavPace + PARefineCounter*PARefinePace))/3600
  val orderT_1Users = ((OrderT * 0.4)*(PDPPace + AddToBagPace + BillingPace))/3600
  val orderT_2Users = ((OrderT * 0.3)*((PDPPace + AddToBagPace)*2 + BillingPace))/3600
  val orderT_5Users = ((OrderT * 0.1)*((PDPPace + AddToBagPace)*5 + BillingPace))/3600
  val orderT_10Users = ((OrderT * 0.1)*((PDPPace + AddToBagPace)*10 + BillingPace))/3600
  val orderT_15Users = ((OrderT * 0.1)*((PDPPace + AddToBagPace)*15 + BillingPace))/3600
  val BillingUsers = ((BillingT - OrderT)*(PDPPace + AddToBagPace + BillingPace))/3600
  val AddToBagTran = ShoppingBagT - (OrderT * 0.4 + OrderT * 0.3 * 2 + OrderT * 0.1 * 5 + OrderT * 0.1 * 10 + OrderT * 0.1 * 15 + (BillingT - OrderT))
  val AddToBagUsers = (AddToBagTran * (PDPPace + AddToBagPace))/3600
  val PDPTran = PDPT - (OrderT * 0.4 + OrderT * 0.3 * 2 + OrderT * 0.1 * 5 + OrderT * 0.1 * 10 + OrderT * 0.1 * 15 + (BillingT - OrderT) + AddToBagTran)
  val PDPUsers = (PDPTran * PDPPace)/3600

}