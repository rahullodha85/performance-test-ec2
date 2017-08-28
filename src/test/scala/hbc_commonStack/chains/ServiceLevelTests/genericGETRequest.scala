package hbc_commonStack.chains.ServiceLevelTests

import hbc_commonStack.base.commonStack
import io.gatling.core.Predef._
import io.gatling.http.Predef._
/**
  * Created by aindana on 4/5/2017.
  */
object genericGETRequest extends commonStack{

  lazy val url = System.getProperty("Url").replaceAll("\"","")

  lazy val transactionName = System.getProperty("TName").replaceAll("\"","")

  lazy val regexCheck = System.getProperty("RegexCheck").replaceAll("\"","")

  def test = group("AdobeTest_Transaction") {
      exec(http("AdobeTest")
        .get(url)
        .check(status.is(200))
        .check(regex(regexCheck).exists)
        //     .check(bodyString.saveAs("Response"))
      )

  }
}
