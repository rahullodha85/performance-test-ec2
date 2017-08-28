package hbc_theBay.theBay_scenarios


import hbc_theBay.theBay_base.{clearSession, theBay, theBayLoadSettings, theBayWLM}
import hbc_theBay.thebay_chains.theBay_Checkout._
import hbc_theBay.thebay_chains.theBay_PDPChains._
import hbc_theBay.thebay_chains.theBay_SearchString.theBaySearchString
import io.gatling.core.Predef._
import io.gatling.core.structure.ChainBuilder

import scala.language.postfixOps

/**
  * Created by aindana on 3/14/2016.
  */
object chainBuilders extends theBay with theBayLoadSettings with theBayWLM{

  def initialize = clearSession.clearSession

  def homePageChain = theBayHomePage.HomePage

  def paChain(PALink: String = "None"): ChainBuilder = theBaySearchPA.PA

  def pdpChain: ChainBuilder = theBayPDPPre.PDPPre exec theBayPDP.PDP

  def searchTermChain: ChainBuilder = theBaySearchString.Search_String

  def billingChain: ChainBuilder = theBayChgQty.ChgQty exec theBayCheckout.Checkout exec theBayShipping.Shipping exec theBayBilling.Billing

  def guestCheckoutChain: ChainBuilder =  theBayChgQty.ChgQty exec theBayCheckout.Checkout exec theBayChgQty.ChgQty exec theBayCheckout.Checkout exec theBayShipping.Shipping

  def addToBagChain(ItemQty:String="1"): ChainBuilder = theBayAddToBag.AddToBag

  def HomePage() = {
    val HomePage = during(duration) {
      pace(HomePagePace)
        .exitBlockOnFail {
          exec(initialize)
            .exec(homePageChain)
        }
    }

    HomePage
  }


  def SearchTerm() = {
    val SearchTerm = during(duration) {
      pace(SearchTermPace)
        .exitBlockOnFail {
          exec(initialize)
            .exec(searchTermChain)
        }
    }

    SearchTerm
  }


  def PDP() = {
    val PDP = during(duration) {
      pace(PDPPace)
        .exitBlockOnFail {
          exec(initialize)
            .exec(pdpChain)
        }
    }

    PDP
  }

  def AddToBag(numOfItems: Int =1) = {
    val AddToBag = during(duration) {
      pace(PDPPace + AddToBagPace*numOfItems)
        .exitBlockOnFail {
          exec(initialize)
            .asLongAs(session => session("numOfItems").as[Int] < numOfItems) {
              exec(pdpChain)
                .exec(addToBagChain())
            }
        }
    }

    AddToBag
  }


  def Billing(numOfItems: Int =1) = {
    val Billing = during(duration) {
      pace(PDPPace + AddToBagPace*numOfItems + BillingPace)
        .exitBlockOnFail {
          exec(initialize)
            .asLongAs(session => session("numOfItems").as[Int] < numOfItems) {
              exec(pdpChain)
                .exec(addToBagChain())
            }
            .exec(billingChain)
        }
    }

    Billing
  }



  def guestCheckout(numOfItems: Int =1, synthetic: Boolean = false) = {

    val SubmitOrder = if(synthetic) {
      exitBlockOnFail {
        exec(initialize)
          .asLongAs(session => session("numOfItems").as[Int] < numOfItems) {
            exec(pdpChain)
              .exec(addToBagChain())
          }
          .exec(guestCheckoutChain)
      }
    }
    else {
      during(duration) {
      //  pace((PDPPace + AddToBagPace) * numOfItems + BillingPace)
        //  .
        exitBlockOnFail {
            exec(initialize)
              .asLongAs(session => session("numOfItems").as[Int] < numOfItems) {
                exec(pdpChain)
                  .exec(addToBagChain())
              }
              .exec(guestCheckoutChain)
          }
      }
    }

    SubmitOrder
  }



}