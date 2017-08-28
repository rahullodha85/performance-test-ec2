package hbc_commonStack.scenarios

import hbc_commonStack.base.commonStack
import io.gatling.core.Predef._
import io.gatling.core.structure.{ChainBuilder, ScenarioBuilder}

import scala.language.postfixOps

/**
  * Created by aindana on 3/14/2016.
  */
class scenarioBuilders(name:String="Default") extends commonStack{

  def newScenario = scenario(name)

  def createScenario(f: => ScenarioBuilder)(chain: => ChainBuilder): ScenarioBuilder = f exec chain

  def PDPService = createScenario(newScenario)(chainBuilders.PDPServiceTest)

  def CSService = createScenario(newScenario)(chainBuilders.ColorServiceTest)

  def genericGET = createScenario(newScenario)(chainBuilders.genericGETRequestTest)

  def placeOrders = createScenario(newScenario)(chainBuilders.guestCheckout2())

  def createAccount(rep:Int=1) = createScenario(newScenario)(chainBuilders.createAccount(rep))

  def HomePage = createScenario(newScenario)(chainBuilders.HomePage_())

  def SearchTerm = createScenario(newScenario)(chainBuilders.SearchTerm())

  def PARefine(synthetic: Boolean = false) = createScenario(newScenario)(chainBuilders.PARefine_(synthetic))

  def PDP = createScenario(newScenario)(chainBuilders.PDP_())

  def AddToBag = createScenario(newScenario)(chainBuilders.AddToBag_())

  def Billing = createScenario(newScenario)(chainBuilders.Billing())

  def RemoveItemsFromBag = createScenario(newScenario)(chainBuilders.RemoveItemsFromBag())

  def regCheckout(numOfItems: Int =1,synthetic: Boolean = false) = createScenario(newScenario)(chainBuilders.registeredCheckout(numOfItems,synthetic))

  def guestCheckout(numOfItems: Int =1,synthetic: Boolean = false) = createScenario(newScenario)(chainBuilders.guestCheckout(numOfItems,synthetic))

}

object scenarioBuilders {

  def apply(name: String = "Default") = new scenarioBuilders(name)
}
