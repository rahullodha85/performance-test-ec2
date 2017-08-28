package hbc_newLT.newLT_chains.newLT_checkout

import java.util.Random

import hbc_newLT.newLT_base.newLT
import io.gatling.core.Predef._
import io.gatling.http.Predef._

/**
  * Created by aindana on 3/14/2016.
  */
object newLTHomePage extends newLT{

  def HomePage = group("HomePage_Transaction") {
    exec(http("/")
      .get(getbasehttp() + "/")
      .check(status.is(200))
    )

    .exec(http("HomePage")
      .get(getbasehttp() + "/Entry.jsp")
        .check(regex("""name="bmUID" value="(.*)"""").saveAs("bmUID"))
        .check(regex("""name="bmFormID" value="(.*)"""").saveAs("bmFormID"))
        .check(regex("""name="sid" value="(.*)"""").saveAs("sid"))
        .check(regex("""<li>\s+<a href="(\S+)">""").findAll.saveAs("PALinks"))
        .check(status.is(200))
    )

      .exec(http("EML1145Acollect.jsp")
      .get(getbasehttp() + "/email_popup/EML1145Acollect.jsp")
        .check(status.is(200))


     )

      .exec(session => {
        val rand = new Random(System currentTimeMillis)
        val PALinksList: List[String] = session("PALinks").as[List[String]]
          val random_index = rand nextInt (PALinksList length)
          var result = PALinksList(random_index)
        while(result.contains("category"))
          {
            result = PALinksList(rand nextInt (PALinksList length))
          }
        session.set("PALink", result)

      })

      .exec(session => {
        insertGroupResponseTimeToInfluxDB(session toString, "HomePage")
        insertGroupResponseTimeToNewRelic(session toString, "HomePage")
        session

      })

    }


  }
