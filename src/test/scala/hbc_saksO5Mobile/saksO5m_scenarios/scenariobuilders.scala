package hbc_saksO5Mobile.saksO5m_scenarios

import hbc_saksO5Mobile.saksO5m_base.SaksO5Mobile
import io.gatling.core.Predef._
import io.gatling.core.structure.{ChainBuilder, ScenarioBuilder}


import scala.language.postfixOps

/**
  * Created by aindana on 3/14/2016.
  */
class scenarioBuilders(name:String="Default") extends SaksO5Mobile{

  def newScenario = scenario(name)

  def createScenario(f: => ScenarioBuilder)(chain: => ChainBuilder): ScenarioBuilder = f exec chain

  def HomePage = createScenario(newScenario)(chainBuilders.HomePage())

  def SearchTerm = createScenario(newScenario)(chainBuilders.SearchTerm())

  def PARefine = createScenario(newScenario)(chainBuilders.PARefine())

  def PDP = createScenario(newScenario)(chainBuilders.PDP())

  def AddToBag = createScenario(newScenario)(chainBuilders.AddToBag())

  def Billing = createScenario(newScenario)(chainBuilders.Billing())

  def SubmitOrder(numOfItems: Int) = createScenario(newScenario)(chainBuilders.SubmitOrder(numOfItems))

}

object scenarioBuilders {

  def apply(name: String = "Default") = new scenarioBuilders(name)
}
