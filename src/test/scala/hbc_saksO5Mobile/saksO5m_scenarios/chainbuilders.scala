package hbc_saksO5Mobile.saksO5m_scenarios

import hbc_saksO5Mobile.saksO5m_base._
import hbc_saksO5Mobile.saksO5m_chains.saksO5m_checkout._
import io.gatling.core.structure.ChainBuilder
import io.gatling.core.Predef._

import scala.language.postfixOps

/**
  * Created by aindana on 3/14/2016.
  */
object chainBuilders extends SaksO5Mobile with saksO5mLoadSettings with saksO5mWLM{

  def initialize = clearSession.clearSession exec setCookie.setCookieSaks

  def homePageChain = SaksO5MobileHomePage.HomePage

  def paChain(PALink: String = "None"): ChainBuilder = saksO5MobilePA.PA(PALink)

  def pdpChain: ChainBuilder = SaksO5MobilePDPPre.PDPPre exec SaksO5MobilePDP.PDP

  def addToBagChain(ItemQty:String="1"): ChainBuilder =
    doIf(session => session("SoldOut").as[Int] == 0) {
      doIf(session => session("sku_idCount").as[Int] > 0) {
        doIf(session => session("availableItems").as[Int] == 1) {
        exec(SaksO5MobileAddToBag.AddToBag(ItemQty))
      }
    }
  }

  def billingChain: ChainBuilder = SaksO5MobileShoppingBag.ShoppingBag exec SaksO5MobileChangeQty.ChangeQty() exec SaksO5MobileCheckout.Checkout exec SaksO5MobileShippingAddress.Shipping exec SaksO5MobileBillingAddress.Billing

  def submitOrderChain: ChainBuilder = billingChain

  def paRefineChain: ChainBuilder = SaksO5MobileFacetSearch.FacetSearch

  def searchTermChain: ChainBuilder = SaksO5MobileTextSearch.TextSearch
  // SaksO5MobileAddToBag.AddToBag()


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

  def PARefine() = {
    val PARefine = during(duration) {
      pace(HomePagePace + TopNavPace + PARefineCounter*PARefinePace)
        .exitBlockOnFail {
          exec(initialize)
            .exec(homePageChain)
            .exec(paChain())
            .repeat(PARefineCounter) {
              exec(paRefineChain)
            }
        }
    }

    PARefine
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

  def SubmitOrder(numOfItems: Int =1) = {
    val SubmitOrder = during(duration) {
      pace((PDPPace + AddToBagPace)*numOfItems + BillingPace)
        .exitBlockOnFail {
          exec(initialize)
            .asLongAs(session => session("numOfItems").as[Int] < numOfItems) {
              exec(pdpChain)
                .exec(addToBagChain())
            }
            .exec(submitOrderChain)
        }
    }

    SubmitOrder
  }
}