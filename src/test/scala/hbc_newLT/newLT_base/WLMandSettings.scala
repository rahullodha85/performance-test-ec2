package hbc_newLT.newLT_base

import com.typesafe.config.ConfigFactory
import io.gatling.core.Predef._

/**
  * Created by aindana on 11/1/2016.
  */

trait newLTSettings {
  lazy val SearchString = jsonFile("src/test/resources/data/hbc_newLT/SearchString.json").random
  lazy val BillingDetails = jsonFile("src/test/resources/data/hbc_newLT/BillingDetails.json").circular
  lazy val ShippingDetails = jsonFile("src/test/resources/data/hbc_newLT/ShippingDetails.json").circular
  lazy val changeQty = jsonFile("src/test/resources/data/hbc_newLT/ChangeQty.json").circular
  lazy val userInfo = jsonFile("src/test/resources/data/hbc_newLT/UserInformation.json").random
}

trait newLTLoadSettings {

  val nothing: Int = System.getProperty("Nothing").toInt
  val duration: Int = System.getProperty("Duration").toInt
  val rampUp: Int = System.getProperty("RampUp").toInt

}

trait newLTWLM {
  val percent: Double = System.getProperty("Percent").toDouble/100
  val guestRatio = 0.6
  val regRatio = 0.4
  val order1Ratio = 0.4
  val order2Ratio = 0.3
  val order5Ratio = 0.1
  val order10Ratio = 0.1
  val order15Ratio = 0.1


  val config = ConfigFactory.load("data/hbc_newLT/WLM")

  //Peak Black Friday Load of 2016
  /*val HomePageT= 26035*percent
  val TextSearchT = 33097*percent
  val PDPT = 39896*percent
  val ShoppingBagT = 16525*percent
  val BillingT= 4437*percent
  val OrderT = 2890*percent
  val PAMenuT = 25348*percent
  val PARefineT= 73738*percent
  val PromoT = 1821*percent
  val cartAddT = 40556*percent
  val cartRemT = 17642*percent*/

  //Peak Black Friday Load of 2015 from Funnel Diagram
  /*val HomePageT= 19408*percent
  val TextSearchT = 11075*percent
  val PDPT = 39896*percent
  val ShoppingBagT = 7424*percent
  val BillingT= 2010*percent
  val OrderT = 1554*percent
  val PAMenuT = 14362*percent
  val PARefineT= 15958*percent
  val PromoT = 1821*percent
  val cartAddT = 30417*percent
  val cartRemT = 17642*percent*/

  //Normal load from LT
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

}