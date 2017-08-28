package hbc_newLT.newLT_testRunner

import hbc_loadTests.{PopulatedScenario, loadTestingSettings}
import hbc_newLT.newLT_base.{newLTLoadSettings, newLTSettings, newLTWLM}
import hbc_newLT.newLT_scenarios.scenarioBuilders
import io.gatling.core.Predef._

import scala.language.postfixOps

/**
  * Created by aindana on 3/14/2016.
  */
class AkamaiTest extends Simulation with newLTSettings with newLTWLM with newLTLoadSettings with loadTestingSettings{

  val run = PopulatedScenario(silentRes,disableCache,rampUp,nothing)

  val AkamaiTest = run.loadRampUpScenarioWithUsers(scenarioBuilders("AkamaiTest").AkamaiTest,10)


  setUp(
    // run async(HomePage,TopNav_PARefine,SearchTerm,PDP,AddToBag,RemoveItemsBag,Billing,GuestOrders_1,GuestOrders_2,GuestOrders_5,GuestOrders_10,GuestOrders_15,RegOrders_1,RegOrders_2,RegOrders_5,RegOrders_10,RegOrders_15)
     //run async(GuestOrders_1,RegOrders_1)
    run async(AkamaiTest)
  )


}

