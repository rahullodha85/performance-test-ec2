package hbc_commonStack.chains.pa

import hbc_newLT.newLT_base.newLT
import hbc_commonStack.base.commonStack
import io.gatling.core.Predef._
import io.gatling.http.Predef._

/**
  * Created by aindana on 3/14/2016.
  */
object PA extends commonStack {

  lazy val baseHttpUrl = if(PA_HTTPS) getbasehttps() else getbasehttp()

  def PA(PALink: String = "None") = group("ProductArray_Transaction") {
    exec(session => {

      var PALink = session("PALink").as[String]
      if(!session("PALink").as[String].contains("http"))
      {
        PALink = baseHttpUrl + session("PALink").as[String]
      }

      session.set("PALink",PALink)

    })

        .exec(http("TopNav")
        .get("${PALink}")
        .check(regex("""folder_id=(\d+)""").findAll.saveAs("folder_id_list"))
          .check(regex("""id="baseRefinementUrl" value="(\S+)"""").count.saveAs("baseRefinementUrl_count"))
          .check(regex("""id="baseRefinementUrl" value="(\S+)"""").findAll.saveAs("baseRefinementUrl"))
          .check(regex("""id="nonRefinementsParameter" value="(\d+)"""").findAll.saveAs("nonRefinementsParameter"))
          .check(regex("""id="refinement-(\d+)" name="available""").findAll.saveAs("PARefineList"))
          .check(status.is(200))
        )


        .exec(session => {
      //    println(session("PALink").as[String])
          insertGroupResponseTimeToInfluxDB(session toString, "TopNav")
          insertGroupResponseTimeToNewRelic(session toString, "TopNav")
          session

        })
  }


}