package hbc_newLT.newLT_chains.newLT_pa

import hbc_newLT.newLT_base.newLT
import io.gatling.core.Predef._
import io.gatling.http.Predef._

/**
  * Created by aindana on 3/14/2016.
  */
object newLTPA extends newLT {

  def PA(PALink: String = "None") = group("ProductArray_Transaction") {
    exec(session => {
      if(!PALink.equals("None"))
        {
          session.set("PALink",PALink)
        }

   /*   if(!(session("PALink").as[String]).startsWith("/"))
     {
      session.set("PALink",(getbasehttp() + session("PALink").as[String]))
     }
     else if(!(session("PALink").as[String]).contains(baseUrl))
      {
        session.set("PALink",(getbasehttp() + session("PALink").as[String]))
      }*/
     // println(session("PALink").as[String])
      session
    })

        .exec(http("TopNav")
        .get(getbasehttp() + "${PALink}")
        .check(regex("""folder_id=(\d+)""").saveAs("folder_id"))
          .check(regex("""id="baseRefinementUrl" value="(\S+)"""").saveAs("baseRefinementUrl"))
          .check(regex("""id="nonRefinementsParameter" value="(\d+)"""").saveAs("nonRefinementsParameter"))
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
