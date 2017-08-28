package hbc_commonStack.chains.pdp

import java.util.Random

import hbc_newLT.newLT_base.newLT
import hbc_commonStack.base.commonStack
import io.gatling.core.Predef._
import io.gatling.http.Predef._

/**
  * Created by aindana on 3/14/2016.
  */
object PDPPre extends commonStack {

  lazy val baseHttpUrl = if(Search_HTTPS) getbasehttps() else getbasehttp()

  def PDPPre = group("Pre_Transaction") {
    doIfOrElse(session => session("PDPincementer").as[Int] == 0) {
      /*exec(http("Goto All Products")
        .get(getbasehttp() + "/search/EndecaSearch.jsp?bmForm=endeca_search_form_one&submit-search=&bmSingle=N_Dim&N_Dim=0&bmHidden=Ntk&Ntk=Entire+Site&bmHidden=Ntx&Ntx=mode%2Bmatchpartialmax&SearchString=&Ntt=")
        .check(regex("""totalNumberOfPages pagination-count">(\d+)</span>""").saveAs("TotalPages"))
        .check(status.is(200))
      )*/

        exec(session => {

          val rand = new Random(System.currentTimeMillis());
      //    val total = session("TotalPages").as[String].toInt
          val total = 10
          val random_index = (rand nextInt (total)) * 60
          session.set("randIndex", random_index)

        })

           .tryMax(5) {
           exec(http("Pick a Product")
               .get(baseHttpUrl + "/search/EndecaSearch.jsp?Nao=${randIndex}&Ntx=mode%2Bmatchpartialmax&Ntk=Entire+Site&bmForm=endeca_search_form_one&N=0&bmHidden=Ntk&bmHidden=Ntx&N_Dim=0&bmSingle=N_Dim")
     //        .get(getbasehttp() + "/search/EndecaSearch.jsp?Nao=${randIndex}&Ntx=mode%2Bmatchpartialmax&Ntk=Entire+Site&bmForm=endeca_search_form_one&N=0&bmHidden=Ntk&bmHidden=Ntx&N_Dim=0&bmSingle=N_Dim")
             .check(regex("""folder_id=(\d+)""").saveAs("folder_id"))
             .check(regex("""bmUID=(.*)"""").saveAs("bmUID"))
          //   .check(regex("""sid" value="(.*)"""").saveAs("sid"))
             .check(regex("""<a id="image-url-\d+" href="(\S+)">""").findAll.saveAs("PDPLinks"))
             .check(status.is(200))
           )
         }

        .exec(session => {
          val PDPLinksList: List[String] = session("PDPLinks").as[List[String]]
          val PDPLink = PDPLinksList(0)
          session.set("PDPLink", PDPLink).set("PDPincementer", 1)
        })


    } {
      exec(session => {

        val PDPLinksList: List[String] = session("PDPLinks").as[List[String]]
        val PDPincementer = session("PDPincementer").as[Int]
        val PDPLink = PDPLinksList(PDPincementer)
        val incrementer = if (PDPincementer + 1 < PDPLinksList.length) PDPincementer + 1 else 0
        println("PDPLink" + PDPLink)
        session.set("PDPLink", PDPLink).set("PDPincementer", incrementer)
      })
    }
  }
}