package hbc_commonStack.testRunner

import hbc_loadTests.{PopulatedScenario, loadTestingSettings}
import hbc_commonStack.base.{LoadSettings, DataSettings, WLMSettings}
import hbc_commonStack.scenarios.scenarioBuilders
import io.gatling.core.Predef._

import scala.language.postfixOps

/**
  * Created by aindana on 3/14/2016.
  */
class colorSwatchService extends Simulation with DataSettings with WLMSettings with LoadSettings with loadTestingSettings{

  val test = PopulatedScenario(silentRes,disableCache,rampUp,nothing)

  // val regCheckout = test.loadRampUpScenarioWithUsers(scenarioBuilders("createAccount").regCheckout(1,true),1)

  val CS = test.loadRampUpScenarioWithUsers(scenarioBuilders("ColorSwatchServiceLevelTest").CSService,CSServiceUsers)


  setUp(
    test async(CS)
  )


}

