package hbc_theBay.theBay_scenarios

import hbc_theBay.theBay_base.theBay
import io.gatling.core.Predef._
import io.gatling.core.structure.{ChainBuilder, ScenarioBuilder}

import scala.language.postfixOps

/**
  * Created by aindana on 3/14/2016.
  */
class scenarioBuilders(name:String="Default") extends theBay{

  def newScenario = scenario(name)

  def createScenario(f: => ScenarioBuilder)(chain: => ChainBuilder): ScenarioBuilder = f exec chain

  def HomePage = createScenario(newScenario)(chainBuilders.HomePage())

  def SearchTerm = createScenario(newScenario)(chainBuilders.SearchTerm())


  def PDP = createScenario(newScenario)(chainBuilders.PDP())

  def AddToBag = createScenario(newScenario)(chainBuilders.AddToBag())

 // def Billing = createScenario(newScenario)(chainBuilders.Billing())

  def guestCheckout(numOfItems: Int =1,synthetic: Boolean = false) = createScenario(newScenario)(chainBuilders.guestCheckout(numOfItems,synthetic))

}

object scenarioBuilders {

  def apply(name: String = "Default") = new scenarioBuilders(name)
}
