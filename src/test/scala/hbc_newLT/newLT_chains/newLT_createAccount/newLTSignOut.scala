package hbc_newLT.newLT_chains.newLT_createAccount

import hbc_newLT.newLT_base.newLT
import io.gatling.core.Predef._
import io.gatling.http.Predef._

/**
  * Created by aindana on 3/14/2016.
  */
object newLTSignOut extends newLT{
 // val ProductID = jsonFile("src/test/resources/data/ProductID.json").circular
  lazy val login = getbasehttps() + "/account/login.jsp"
  lazy val registration = getbasehttps() + "/account/registration.jsp"


  def signOut = group("SignOut_Transaction") {
      exec(http("account/login.jsp")
        .post(login)
          .body(StringBody("bmForm=logout_session_and_cookie"))
        .check(status.is(200))

      )



          .exec(session => {
            insertGroupResponseTimeToInfluxDB(session toString, "SignOut")
            insertGroupResponseTimeToNewRelic(session toString, "SignOut")
            session

          })


      }
  }

