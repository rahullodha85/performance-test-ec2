package hbc_commonStack.chains.createAccount

import hbc_newLT.newLT_base.newLT
import hbc_commonStack.base.commonStack
import io.gatling.core.Predef._
import io.gatling.http.Predef._

/**
  * Created by aindana on 3/14/2016.
  */
object CreateAccount extends commonStack{
 // val ProductID = jsonFile("src/test/resources/data/ProductID.json").circular
  lazy val login = getbasehttps() + "/account/login.jsp"
  lazy val registration = getbasehttps() + "/account/registration.jsp"


  def CreateAccount = group("CreateAccount_Transaction") {
      exec(session => {
        // print the Session for debugging, don't do that on real Simulations
        val username = "test" +  System.currentTimeMillis() + "@gatling.com"
        val password = "Gatling_" + System.currentTimeMillis()
       // insertAccountData("LTMigration","LT_AccountData",username,password)
        session.set("username", username).set("password",password)
      })


      .exec(http("account/login.jsp")
        .post(login)
          .body(StringBody("bmForm=createAccount&bmIsForm=true&bmPrevTemplate=%2Faccount%2Flogin.jsp&bmSubmit=createAccount&createAccount=CREATE+AN+ACCOUNT"))
        .check(status.is(200))

      )

      .exec(http("registration")
        .post(registration)
        .body(StringBody("bmForm=registration&bmIsForm=true&bmPrevTemplate=%2Faccount%2Fregistration.jsp&bmArch=bmText&bmText=USER_ACCOUNT%3C%3EfirstName&bmArch=bmRequired&bmRequired=USER_ACCOUNT%3C%3EfirstName&bmText=USER_ACCOUNT%3C%3EfirstName&USER_ACCOUNT%3C%3EfirstName=Gatling&bmArch=bmText&bmText=USER_ACCOUNT%3C%3ElastName&bmArch=bmRequired&bmRequired=USER_ACCOUNT%3C%3ElastName&bmText=USER_ACCOUNT%3C%3ElastName&USER_ACCOUNT%3C%3ElastName=Load&bmText=USER_ACCOUNT%3C%3Epassword&bmRequired=USER_ACCOUNT%3C%3Epassword&USER_ACCOUNT%3C%3Epassword=${password}&bmText=USER_ACCOUNT%3C%3EconfirmPassword&bmRequired=USER_ACCOUNT%3C%3EconfirmPassword&USER_ACCOUNT%3C%3EconfirmPassword=${password}&bmText=USER_ACCOUNT%3C%3EATR_passwordHint&USER_ACCOUNT%3C%3EATR_passwordHint=Gatling&bmText=USER_ACCOUNT%3C%3Euserid&bmRequired=USER_ACCOUNT%3C%3Euserid&USER_ACCOUNT%3C%3Euserid=${username}&bmHidden=OMNI_EML_ACC_REGIS&OMNI_EML_ACC_REGIS=${username}&bmArrayIndex=USER_ACCOUNT%3C%3EATR_ReceiveEmail&bmCheckbox=USER_ACCOUNT%3C%3EATR_ReceiveEmail&USER_ACCOUNT%3C%3EATR_ReceiveEmail=true&bmSubmit=registerNewUser&registerNewUser=SAVE&bmHidden=chgEmail&chgEmail=on"))
        .check(status.is(200))

      )



          .exec(session => {
            insertAccountData("LTMigration","O5_AccountData",session("username").as[String],session("password").as[String])
            insertGroupResponseTimeToInfluxDB(session toString, "CreateAccount")
            insertGroupResponseTimeToNewRelic(session toString, "CreateAccount")
            session

          })


      }
  }

