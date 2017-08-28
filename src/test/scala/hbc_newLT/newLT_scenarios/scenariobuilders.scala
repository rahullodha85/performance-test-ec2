package hbc_newLT.newLT_scenarios

import hbc_newLT.newLT_base.newLT
import io.gatling.core.Predef._
import io.gatling.core.structure.{ChainBuilder, ScenarioBuilder}


import scala.language.postfixOps

/**
  * Created by aindana on 3/14/2016.
  */
class scenarioBuilders(name:String="Default") extends newLT{

  def newScenario = scenario(name)

  def createScenario(f: => ScenarioBuilder)(chain: => ChainBuilder): ScenarioBuilder = f exec chain

  def AkamaiTest = createScenario(newScenario)(chainBuilders.AkamaiTestHomePage())

  def HomePage = createScenario(newScenario)(chainBuilders.HomePage())

  def SearchTerm = createScenario(newScenario)(chainBuilders.SearchTerm())

  def PARefine(synthetic: Boolean = false) = createScenario(newScenario)(chainBuilders.PARefine(synthetic))

  def PDP = createScenario(newScenario)(chainBuilders.PDP())

  def AddToBag = createScenario(newScenario)(chainBuilders.AddToBag())

  def Billing = createScenario(newScenario)(chainBuilders.Billing())

  def RemoveItemsFromBag = createScenario(newScenario)(chainBuilders.RemoveItemsFromBag())

  def regCheckout(numOfItems: Int =1,synthetic: Boolean = false) = createScenario(newScenario)(chainBuilders.registeredCheckout(numOfItems,synthetic))

  def guestCheckout(numOfItems: Int =1,synthetic: Boolean = false) = createScenario(newScenario)(chainBuilders.guestCheckout(numOfItems,synthetic))

  def createAccount(rep:Int=1) = createScenario(newScenario)(chainBuilders.createAccount(rep))

}

object scenarioBuilders {

  def apply(name: String = "Default") = new scenarioBuilders(name)
}
