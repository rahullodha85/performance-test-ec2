package hbc_loadTests

import io.gatling.core.Predef._
import io.gatling.core.structure.{ChainBuilder, ScenarioBuilder}

import scala.language.postfixOps

/**
  * Created by aindana on 3/14/2016.
  */
case class ScenarioBuilders(name:String="Default"){

  def newScenario = scenario(name)

  def createScenario(f: => ScenarioBuilder)(chain: => ChainBuilder): ScenarioBuilder = f exec chain

  def createNewScenario(chain: => ChainBuilder): ScenarioBuilder = newScenario exec chain
}

