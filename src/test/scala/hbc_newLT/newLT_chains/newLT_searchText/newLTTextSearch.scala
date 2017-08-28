package hbc_newLT.newLT_chains.newLT_searchText

import hbc_newLT.newLT_base.newLT
import io.gatling.core.Predef._
import io.gatling.http.Predef._

/**
  * Created by aindana on 3/14/2016.
  */
object newLTTextSearch extends newLT {

  def TextSearch = group("TextSearch_Transaction") {
    feed(SearchString)
      .exec(http("lookahead_json")
        .get(getbasehttp() + "/search/lookahead_json.jsp?callback=jQuery172012264395567273178_1458054414738&term=${SearchString}&_=1458054490227")
        .check(status.is(200))
      )

    /*  .exec(session => {

        session.set("SearchStringCheck",session("SearchString").as[String].toUpperCase)

      })
*/

      .exec(http("TextSearch")
        .get(getbasehttps() + "/search/EndecaSearch.jsp?bmForm=endeca_search_form_one&bmFormID=lGiaDZV&bmUID=lGiaDZW&bmIsForm=true&bmPrevTemplate=%2FEntry.jsp&bmText=SearchString&SearchString=${SearchString}&submit-search=&bmSingle=N_Dim&N_Dim=0&bmHidden=Ntk&Ntk=Entire+Site&bmHidden=Ntx&Ntx=mode%2Bmatchpartialmax&bmHidden=prp8&prp8=t15&bmHidden=prp13&prp13=&bmHidden=sid&sid=15AA58CAF60B&bmHidden=FOLDER<>folder_id&FOLDER<>folder_id=")
            .check(bodyString.saveAs("endeca"))
      //      .check(regex("""${SearchStringCheck}""").exists)
        .check(status.is(200))
      )

        .exec(session => {
       //   println("SEARCH || RESULT")
        //  println(session("SearchString").as[String])
        //  println(session("endeca").as[String])
       //   println(session("SearchString").as[String] + "  ||  " + session("searchedtext").as[String])
          insertGroupResponseTimeToInfluxDB(session toString, "TextSearch")
          insertGroupResponseTimeToNewRelic(session toString, "TextSearch")
          session

        })


  }
}
