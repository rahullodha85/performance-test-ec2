
package hbc_commonStack.testRunner

import hbc_loadTests.{PopulatedScenario, loadTestingSettings}
import hbc_commonStack.base.{LoadSettings, DataSettings, WLMSettings}
import hbc_commonStack.scenarios.scenarioBuilders
import io.gatling.core.Predef._

import scala.language.postfixOps

/**
  * Created by aindana on 3/14/2016.
  */
class LocalLoadTests extends Simulation with DataSettings with WLMSettings with LoadSettings with loadTestingSettings{

  val run = PopulatedScenario(silentRes,disableCache,rampUp,nothing)

  val HomePage = run.loadRampUpScenarioWithUsers(scenarioBuilders("HomePage").HomePage,HomePageUsers)
  val TopNav_PARefine = run.loadRampUpScenarioWithUsers(scenarioBuilders("TopNav_PA_PARefine").PARefine(),TopNavUsers)
  val SearchTerm = run.loadRampUpScenarioWithUsers(scenarioBuilders("SearchTerm").SearchTerm,SearchTermUsers)
  val PDP = run.loadRampUpScenarioWithUsers(scenarioBuilders("PDP").PDP,PDPUsers)
 
  setUp(
     run async(
       HomePage,
       TopNav_PARefine,
       SearchTerm,
       PDP
     )
  )


}
