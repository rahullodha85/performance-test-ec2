package hbc_commonStack.chains.checkout

import java.util.Random

import hbc_newLT.newLT_base.newLT
import hbc_commonStack.base.commonStack
import io.gatling.core.Predef._
import io.gatling.http.Predef._

/**
  * Created by aindana on 3/14/2016.
  */
object HomePage extends commonStack{

  lazy val baseHttpUrl = if(HomePage_HTTPS) getbasehttps() else getbasehttp()

  def HomePage = group("HomePage_Transaction") {
    exec(http("/")
      .get(baseHttpUrl + "/")
      .check(status.is(200))
    )

    .exec(http("HomePage")
      .get(baseHttpUrl + "/Entry.jsp")
        .check(regex("""name="bmUID" value="(.*)"""").saveAs("bmUID"))
        .check(regex("""name="bmFormID" value="(.*)"""").saveAs("bmFormID"))
       // .check(regex("""sid=(\S+)&"""").saveAs("sid"))
        .check(regex("""<li class="header-nav-flyout__list-item" data-reactid="\d+"><a class="header-nav-flyout__link" href="(\S+)" data-reactid="\d+">\S+<\/a><\/li>""").findAll.saveAs("PALinks"))
        .check(status.is(200))
    )

      .exec(http("EML1145Acollect.jsp")
      .get(baseHttpUrl + "/email_popup/EML1145Acollect.jsp")
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
