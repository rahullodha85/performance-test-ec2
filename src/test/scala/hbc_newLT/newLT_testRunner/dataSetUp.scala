package hbc_newLT.newLT_testRunner

import hbc_loadTests.{PopulatedScenario, loadTestingSettings}
import hbc_newLT.newLT_base.{newLTLoadSettings, newLTSettings, newLTWLM}
import hbc_newLT.newLT_scenarios.scenarioBuilders
import io.gatling.core.Predef._

import scala.language.postfixOps

/**
  * Created by aindana on 3/14/2016.
  */
class dataSetUp extends Simulation with newLTSettings with newLTWLM with newLTLoadSettings with loadTestingSettings{

  val data = PopulatedScenario(true,true,0,0)

  val CreateAccount = data.loadRampUpScenarioWithUsers(scenarioBuilders("createAccount").createAccount(40),3)

  setUp(
    data async(CreateAccount)
  )


}

