package hbc_newLT.newLT_testRunner

import hbc_loadTests.{PopulatedScenario, loadTestingSettings}
import hbc_newLT.newLT_base.{newLTLoadSettings, newLTSettings, newLTWLM}
import hbc_newLT.newLT_scenarios.scenarioBuilders
import io.gatling.core.Predef._

import scala.language.postfixOps

/**
  * Created by aindana on 3/14/2016.
  */
class orderCreation extends Simulation with newLTSettings with newLTWLM with newLTLoadSettings with loadTestingSettings{

  val run = PopulatedScenario(silentRes,disableCache,rampUp,nothing)


  val GuestOrders_1 = run.loadRampUpScenarioWithUsers(scenarioBuilders("GuestUser_1_Item_SubmitOrder").guestCheckout(1),50)
  val GuestOrders_2 = run.loadRampUpScenarioWithUsers(scenarioBuilders("GuestUser_2_Item_SubmitOrder").guestCheckout(2),10)
  val GuestOrders_5 = run.loadRampUpScenarioWithUsers(scenarioBuilders("GuestUser_5_Item_SubmitOrder").guestCheckout(5),10)
  val GuestOrders_10 = run.loadRampUpScenarioWithUsers(scenarioBuilders("GuestUser_10_Item_SubmitOrder").guestCheckout(10),5)
  val GuestOrders_15 = run.loadRampUpScenarioWithUsers(scenarioBuilders("GuestUser_15_Item_SubmitOrder").guestCheckout(15),5)

  setUp(
     run async(

       GuestOrders_1,
       GuestOrders_2,
       GuestOrders_5,
       GuestOrders_10,
       GuestOrders_15

     )
 //    run async(SearchTerm)
  )


}

