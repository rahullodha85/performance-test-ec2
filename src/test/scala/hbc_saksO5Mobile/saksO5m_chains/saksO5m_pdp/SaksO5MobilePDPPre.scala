package hbc_saksO5Mobile.saksO5m_chains.saksO5m_checkout

import java.util.Random

import BaseTest.BaseTest
import hbc_saksO5Mobile.saksO5m_base.SaksO5Mobile
import io.gatling.core.Predef._
import io.gatling.http.Predef._

/**
  * Created by aindana on 3/14/2016.
  */
object SaksO5MobilePDPPre extends SaksO5Mobile {

  def PDPPre = group("Pre_Transaction") {
    doIfOrElse(session => session("PDPincementer").as[Int] == 0) {
      exec(http("Goto All Products")
        .get(getbasehttp() + "/eSearch.jsp?SearchString=+&EndecaSearchButton=&N_Dim=0&Ntt=&Ns=&N=0&Ntk=Entire+Site&Ntx=mode%2Bmatchpartialmax&prp8=t15&prp13=&PA=TRUE")
        //  .check(bodyString.saveAs("ProdArray"))
        .check(regex("""totalNumberOfPages">(\d+)</span>""").saveAs("TotalPages"))
      )

        .exec(session => {

          val rand = new Random(System.currentTimeMillis());
          val total = session("TotalPages").as[String].toInt
          val random_index = (rand nextInt (total)) * 60
          session.set("randIndex", random_index)

        })

        .exec(http("Random Page and Product")
          .get(getbasehttp() + "/eSearch.jsp?Nao=${randIndex}&prp8=t15&Ntx=mode%2Bmatchpartialmax&Ntk=Entire+Site&N=0&PA=TRUE&N_Dim=0")
          .check(regex("""<a class="sfa-pa-product-with-swatches" href="(\S+)"""").findAll.saveAs("PDPLinks"))
          .check(regex("""li id="(\d+)"""").findAll.saveAs("ProductIDAll")))

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
        session.set("PDPLink", PDPLink).set("PDPincementer", incrementer)
      })
    }
  }
}