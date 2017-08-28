package hbc_newLT.newLT_testRunner

import hbc_loadTests.{loadTestingSettings, PopulatedScenario}
import hbc_newLT.newLT_base.{newLTSettings, newLTLoadSettings, newLTWLM}
import hbc_newLT.newLT_scenarios.scenarioBuilders
import io.gatling.core.Predef._

import scala.language.postfixOps

/**
  * Created by aindana on 3/14/2016.
  */
class testRunner extends Simulation with newLTSettings with newLTWLM with newLTLoadSettings with loadTestingSettings{

  val run = PopulatedScenario(silentRes,disableCache,rampUp,nothing)

  val HomePage = run.loadRampUpScenarioWithUsers(scenarioBuilders("HomePage").HomePage,HomePageUsers)
  val TopNav_PARefine = run.loadRampUpScenarioWithUsers(scenarioBuilders("TopNav_PA_PARefine").PARefine(),TopNavUsers)
  val SearchTerm = run.loadRampUpScenarioWithUsers(scenarioBuilders("SearchTerm").SearchTerm,SearchTermUsers)
  val PDP = run.loadRampUpScenarioWithUsers(scenarioBuilders("PDP").PDP,PDPUsers)
  val AddToBag = run.loadRampUpScenarioWithUsers(scenarioBuilders("AddToBag").AddToBag,AddToBagUsers)
  val RemoveItemsBag = run.loadRampUpScenarioWithUsers(scenarioBuilders("RemoveItemsFromBag").RemoveItemsFromBag,RemoveItemUsers)
  val Billing = run.loadRampUpScenarioWithUsers(scenarioBuilders("Shipping_and_Billing").Billing,BillingUsers)
  val GuestOrders_1 = run.loadRampUpScenarioWithUsers(scenarioBuilders("GuestUser_1_Item_SubmitOrder").guestCheckout(1),orderT_1Users*guestRatio)
  val GuestOrders_2 = run.loadRampUpScenarioWithUsers(scenarioBuilders("GuestUser_2_Item_SubmitOrder").guestCheckout(2),orderT_2Users*guestRatio)
  val GuestOrders_5 = run.loadRampUpScenarioWithUsers(scenarioBuilders("GuestUser_5_Item_SubmitOrder").guestCheckout(5),orderT_5Users*guestRatio)
  val GuestOrders_10 = run.loadRampUpScenarioWithUsers(scenarioBuilders("GuestUser_10_Item_SubmitOrder").guestCheckout(10),orderT_10Users*guestRatio)
  val GuestOrders_15 = run.loadRampUpScenarioWithUsers(scenarioBuilders("GuestUser_15_Item_SubmitOrder").guestCheckout(15),orderT_15Users*guestRatio)
  val RegOrders_1 = run.loadRampUpScenarioWithUsers(scenarioBuilders("RegUser_1_Item_SubmitOrder").regCheckout(1),orderT_1Users*regRatio)
  val RegOrders_2 = run.loadRampUpScenarioWithUsers(scenarioBuilders("RegUser_2_Item_SubmitOrder").regCheckout(2),orderT_2Users*regRatio)
  val RegOrders_5 = run.loadRampUpScenarioWithUsers(scenarioBuilders("RegUser_5_Item_SubmitOrder").regCheckout(5),orderT_5Users*regRatio)
  val RegOrders_10 = run.loadRampUpScenarioWithUsers(scenarioBuilders("RegUser_10_Item_SubmitOrder").regCheckout(10),orderT_10Users*regRatio)
  val RegOrders_15 = run.loadRampUpScenarioWithUsers(scenarioBuilders("RegUser_15_Item_SubmitOrder").regCheckout(15),orderT_15Users*regRatio)

  setUp(
     run async(
       HomePage,
       TopNav_PARefine,
       SearchTerm,
       PDP,
       AddToBag,
       RemoveItemsBag,
       Billing,
       GuestOrders_1,
       GuestOrders_2,
       GuestOrders_5,
       GuestOrders_10,
       GuestOrders_15,
       RegOrders_1,
       RegOrders_2,
       RegOrders_5,
       RegOrders_10,
       RegOrders_15
     )
 //    run async(SearchTerm)
  )


}

