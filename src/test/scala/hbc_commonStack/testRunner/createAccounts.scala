package hbc_commonStack.testRunner

import hbc_loadTests.{PopulatedScenario, loadTestingSettings}
import hbc_commonStack.base.{LoadSettings, DataSettings, WLMSettings}
import hbc_commonStack.scenarios.scenarioBuilders
import io.gatling.core.Predef._

import scala.language.postfixOps

/**
  * Created by aindana on 3/14/2016.
  */
class createAccounts extends Simulation with DataSettings with WLMSettings with LoadSettings with loadTestingSettings{

  val data = PopulatedScenario(true,true,0,0)

  val CreateAccount = data.loadRampUpScenarioWithUsers(scenarioBuilders("createAccount").createAccount(40),3)

  setUp(
    data async(CreateAccount)
  )


}

