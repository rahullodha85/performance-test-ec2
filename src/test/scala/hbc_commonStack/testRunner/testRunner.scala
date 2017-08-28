package hbc_commonStack.testRunner

import hbc_loadTests.{PopulatedScenario, loadTestingSettings}
import hbc_commonStack.base.{LoadSettings, DataSettings, WLMSettings}
import hbc_commonStack.scenarios.scenarioBuilders
import io.gatling.core.Predef._

import scala.language.postfixOps

/**
  * Created by aindana on 3/14/2016.
  */
class testRunner extends Simulation with DataSettings with WLMSettings with LoadSettings with loadTestingSettings{
  val test = PopulatedScenario(silentRes,disableCache,rampUp,nothing)

  val orders = test.loadRampUpScenarioWithUsers(scenarioBuilders("Place orders").placeOrders,(ordersUsers+1))


  setUp(
    test async(orders)
  )

}

