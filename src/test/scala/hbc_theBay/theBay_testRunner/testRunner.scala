package hbc_theBay.theBay_testRunner

import hbc_loadTests.{PopulatedScenario, loadTestingSettings}
import hbc_theBay.theBay_base.{theBayLoadSettings, theBaySettings, theBayWLM}
import hbc_theBay.theBay_scenarios.scenarioBuilders
import io.gatling.core.Predef._

import scala.language.postfixOps

/**
  * Created by aindana on 3/14/2016.
  */
class testRunner extends Simulation with theBaySettings with theBayWLM with theBayLoadSettings with loadTestingSettings{

  val run = PopulatedScenario(silentRes,disableCache,rampUp,nothing)

  val HomePage = run.loadRampUpScenarioWithUsers(scenarioBuilders("HomePage").HomePage,HomePageUsers)
  val SearchTerm = run.loadRampUpScenarioWithUsers(scenarioBuilders("SearchTerm").SearchTerm,SearchTermUsers)
  val PDP = run.loadRampUpScenarioWithUsers(scenarioBuilders("PDP").PDP,PDPUsers)
  val AddToBag = run.loadRampUpScenarioWithUsers(scenarioBuilders("AddToBag").AddToBag,AddToBagUsers)
  //val Billing = run.loadRampUpScenarioWithUsers(scenarioBuilders("Shipping_and_Billing").Billing,BillingUsers)
  val GuestOrders_1 = run.loadRampUpScenarioWithUsers(scenarioBuilders("GuestUser_1_Item_SubmitOrder").guestCheckout(1),orderT_1Users*guestRatio)
  val GuestOrders_2 = run.loadRampUpScenarioWithUsers(scenarioBuilders("GuestUser_2_Item_SubmitOrder").guestCheckout(2),orderT_2Users*guestRatio)
  val GuestOrders_5 = run.loadRampUpScenarioWithUsers(scenarioBuilders("GuestUser_5_Item_SubmitOrder").guestCheckout(5),orderT_5Users*guestRatio)
  val GuestOrders_10 = run.loadRampUpScenarioWithUsers(scenarioBuilders("GuestUser_10_Item_SubmitOrder").guestCheckout(10),orderT_10Users*guestRatio)
  val GuestOrders_15 = run.loadRampUpScenarioWithUsers(scenarioBuilders("GuestUser_15_Item_SubmitOrder").guestCheckout(15),orderT_15Users*guestRatio)

  setUp(
     run async(
       HomePage,
       SearchTerm,
       PDP,
       AddToBag,
       GuestOrders_1,
       GuestOrders_2,
       GuestOrders_5,
       GuestOrders_10,
       GuestOrders_15
     )
 //    run async(SearchTerm)
  )


}

