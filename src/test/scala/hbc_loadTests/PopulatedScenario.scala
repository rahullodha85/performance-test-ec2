package hbc_loadTests

import _root_.io.gatling.core.result.message.{OK, KO}
import io.gatling.core.Predef._
import io.gatling.core.config.Protocol
import io.gatling.core.structure.{ChainBuilder, PopulatedScenarioBuilder, ScenarioBuilder}
import io.gatling.http.Predef._

import scala.collection.mutable.ArrayBuffer
import scala.language.postfixOps

/**
  * Created by aindana on 3/14/2016.
  */

case class PopulatedScenario(silentRes:Boolean,disableCache:Boolean,rampUp:Int,nothing:Int,numOfUsers:Int =1) {
  private val httpConfig = http
    .acceptHeader("application/json, text/plain, text/javascript, */*; q=0.01")
    .acceptLanguageHeader("en-US,en;q=0.8")
    .connection("keep-alive")
 //   .header("LoadTesting", "Gatling")
 //   .header("Cookie", "PT=Gatling")
    .acceptEncodingHeader("gzip, deflate")
    .userAgentHeader("Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/57.0.2987.110 Safari/537.36")
    .inferHtmlResources(BlackList("""http:\/\/s7d9\.scene7\.com\/is\/image\/TheBay\/534_Footer_Brand_Logo_\S+""",""".*h\.online-etrix\.net.*""",""".*\.cookie.utils.js""",""".*\$SWATCHSMALL\$"""))
    .disableClientSharing


  def getHttpConfig(): Protocol = {
    if (silentRes && disableCache)
      httpConfig.silentResources.disableCaching
    else if (silentRes)
      httpConfig.silentResources
    else if (disableCache)
      httpConfig.disableCaching
    else httpConfig
  }

  def execute(scenario1: ScenarioBuilder, scenario2: ScenarioBuilder): ScenarioBuilder = {
    scenario1 exec scenario2
  }

  def withUsers(numOfUsers: Int=numOfUsers)(scenario: ScenarioBuilder): PopulatedScenarioBuilder = {
    scenario.inject(
      atOnceUsers(numOfUsers)
    ).protocols(getHttpConfig)
  }

  def normalizeUsers(Users:Int) : Int = {
    var newUsers: Int = Users
    if(Users < 1) newUsers = 1
    newUsers
  }

  def rampUpToUsers(numOfUsers: Int=numOfUsers)(rampUp: Int)(nothing: Int)(scenario: ScenarioBuilder): PopulatedScenarioBuilder = {
    scenario.inject(
      nothingFor(nothing),
      rampUsers(normalizeUsers(numOfUsers)) over (rampUp)
    ).protocols(getHttpConfig)
  }

  def serial[A,B](g:(A,A) => A, f:A=>B, sc:List[A]): B = {
    var ini = sc(0)
    for(i<-1 until sc.length)
      ini = g(ini,sc(i))

    f(ini)
  }

  def parallel[A,B](f:A=>B)(sc:List[A]) : List[B] =
  {
    var totalScen = new ArrayBuffer[B]
    for(i<-0 until sc.length)
    {
      totalScen += f(sc(i))
    }

    totalScen.toList
  }

  def g[A,B](f:A=>B)(sc:A) : B = f(sc)
  def async(sc: PopulatedScenarioBuilder*): List[PopulatedScenarioBuilder] = sc.toList
  def loadRampUpScenarioWithUsers(sc:ScenarioBuilder, numOfUsers:Int = numOfUsers,rampUp: Int = rampUp,nothing: Int = nothing) = g(rampUpToUsers(numOfUsers)(rampUp)(nothing))(sc)
  def loadScenarioWithUsers(sc:ScenarioBuilder, numOfUsers:Int = numOfUsers) = g(withUsers(numOfUsers))(sc)
  def async_(sc:ScenarioBuilder*) = parallel(withUsers(numOfUsers))(sc.toList)
  def sync(sc:ScenarioBuilder*):PopulatedScenarioBuilder = serial(execute,withUsers(numOfUsers),sc.toList)

}

