package hbc_saksO5Mobile.saksO5m_testRunner

import hbc_loadTests.{loadTestingSettings, PopulatedScenario}
import hbc_saksO5Mobile.saksO5m_base.{saksO5mLoadSettings, saksO5mSettings, saksO5mWLM}
import hbc_saksO5Mobile.saksO5m_scenarios.scenarioBuilders
import io.gatling.core.Predef._

import scala.language.postfixOps

/**
  * Created by aindana on 3/14/2016.
  */
class testRunner extends Simulation with saksO5mSettings with saksO5mWLM with saksO5mLoadSettings with loadTestingSettings{

  val runMultiple = PopulatedScenario(silentRes,disableCache,rampUp,nothing)

  val HomePage = runMultiple.loadRampUpScenarioWithUsers(scenarioBuilders("HomePage").HomePage,HomePageUsers.toInt)
  val TopNav = runMultiple.loadRampUpScenarioWithUsers(scenarioBuilders("TopNav_PA_PARefine").PARefine,TopNavUsers.toInt)
  val SearchTerm = runMultiple.loadRampUpScenarioWithUsers(scenarioBuilders("SearchTerm").SearchTerm,SearchTermUsers.toInt)
  val PDP = runMultiple.loadRampUpScenarioWithUsers(scenarioBuilders("PDP").PDP,PDPUsers.toInt)
  val AddToBag = runMultiple.loadRampUpScenarioWithUsers(scenarioBuilders("AddToBag").AddToBag,AddToBagUsers.toInt)
  val Billing = runMultiple.loadRampUpScenarioWithUsers(scenarioBuilders("Billing").Billing,BillingUsers.toInt)
  val Orders_1 = runMultiple.loadRampUpScenarioWithUsers(scenarioBuilders("Checkout_1").SubmitOrder(1),orderT_1Users.toInt)
  val Orders_2 = runMultiple.loadRampUpScenarioWithUsers(scenarioBuilders("Checkout_2").SubmitOrder(2),orderT_2Users.toInt)
  val Orders_5 = runMultiple.loadRampUpScenarioWithUsers(scenarioBuilders("Checkout_5").SubmitOrder(5),orderT_5Users.toInt)
  val Orders_10 = runMultiple.loadRampUpScenarioWithUsers(scenarioBuilders("Checkout_10").SubmitOrder(10),orderT_10Users.toInt)
  val Orders_15 = runMultiple.loadRampUpScenarioWithUsers(scenarioBuilders("Checkout_15").SubmitOrder(15),orderT_15Users.toInt)


  setUp(
    runMultiple async(HomePage,TopNav,SearchTerm,PDP,AddToBag,Billing,Orders_1,Orders_2,Orders_5,Orders_10,Orders_15)
  )


}

