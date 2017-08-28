package hbc_newLT.newLT_scenarios

import hbc_newLT.newLT_base.setCookie
import hbc_newLT.newLT_base._
import hbc_newLT.newLT_chains.newLT_AkamaiTest.newLTHomePageAkamaiTest
import hbc_newLT.newLT_chains.newLT_checkout._
import hbc_newLT.newLT_chains.newLT_createAccount.{newLTCreateAccount, newLTSignOut}
import hbc_newLT.newLT_chains.newLT_PARefinements.{newLTFilterBy, newLTPARefine}
import hbc_newLT.newLT_chains.newLT_pa.newLTPA
import hbc_newLT.newLT_chains.newLT_pdp.{newLTPDP, newLTPDPPre}
import hbc_newLT.newLT_chains.newLT_registeredCheckout.{newLTRegBillingAddress, newLTRegShippingAddress, newLTRegisteredCheckout}
import hbc_newLT.newLT_chains.newLT_searchText.newLTTextSearch
import io.gatling.core.Predef._
import io.gatling.core.structure.ChainBuilder

import scala.language.postfixOps

/**
  * Created by aindana on 3/14/2016.
  */
object chainBuilders extends newLT with newLTLoadSettings with newLTWLM{

  def initialize = clearSession.clearSession exec setCookie.setCookieLT

  def AkamaiTest = clearSession.clearSession exec setCookieAkamai.setCookieLT exec newLTHomePageAkamaiTest.HomePage

  def homePageChain = newLTHomePage.HomePage

  def paChain(PALink: String = "None"): ChainBuilder = newLTPA.PA(PALink)

  def pdpChain: ChainBuilder = newLTPDPPre.PDPPre exec newLTPDP.PDP

  def paRefineChain: ChainBuilder = newLTFilterBy.FilterBy exec newLTPARefine.PARefine

  def searchTermChain: ChainBuilder = newLTTextSearch.TextSearch

  def createAccountChain: ChainBuilder = newLTCreateAccount.CreateAccount exec newLTSignOut.signOut

  def billingChain: ChainBuilder = newLTChangeQty.ChangeQty exec newLTShippingMethod.shippingMethod exec newLTCheckout.Checkout exec newLTShippingAddress.Shipping exec newLTBillingAddress.Billing

  //  def guestCheckoutChain: ChainBuilder = newLTChangeQty.ChangeQty exec newLTShippingMethod.shippingMethod exec newLTCheckout.Checkout exec newLTShippingAddress.Shipping exec newLTBillingAddress.Billing exec newLTSubmitOrder.SubmitOrder

  def guestCheckoutChain: ChainBuilder = newLTChangeQty.ChangeQty exec newLTCheckout.Checkout exec newLTShippingAddress.Shipping exec newLTBillingAddress.Billing

  // def RegCheckoutChain: ChainBuilder = newLTChangeQty.ChangeQty exec newLTShippingMethod.shippingMethod exec newLTRegisteredCheckout.Checkout exec newLTRegShippingAddress.Shipping exec newLTRegBillingAddress.Billing exec newLTSubmitOrder.SubmitOrder

  def RegCheckoutChain: ChainBuilder = newLTChangeQty.ChangeQty exec newLTRegisteredCheckout.Checkout exec newLTRegShippingAddress.Shipping exec newLTRegBillingAddress.Billing

  def removeAllItems() =  doIf(session => session("cartItemIDCount").as[Int] > 0) {
          exec(newLTRemoveItem.removeItem)
        }


  def addToBagChain(ItemQty:String="1"): ChainBuilder = {
    tryMax(10) {
      doIf(session => session("SoldOut").as[Int] == 0) {
        doIf(session => session("sku_idCount").as[Int] > 0) {
          doIf(session => session("availableItems").as[Int] == 1) {
            exec(newLTAddToBag.AddToBag(ItemQty))
          }
        }
      }
        .exec(newLTShoppingBag.ShoppingBag)
    }
  }

  def AkamaiTestHomePage() = {
    val HomePage = during(duration) {
      pace(HomePagePace)
        .exitBlockOnFail {
          exec(AkamaiTest)
        }
    }

    HomePage
  }

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

  def PARefine(synthetic: Boolean = false) = {
    val PARefine = if(synthetic) {
      exitBlockOnFail {
        exec(initialize)
          .exec(homePageChain)
          .exec(paChain())
          .repeat(PARefineCounter) {
            exec(paRefineChain)
          }
      }
    }
    else
    {
      during(duration) {
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

  def RemoveItemsFromBag(numOfItems: Int =1) = {
    val RemoveItems = during(duration) {
      pace(PDPPace + (AddToBagPace + RemoveItemPace)*numOfItems)
        .exitBlockOnFail {
          exec(initialize)
            .asLongAs(session => session("numOfItems").as[Int] < numOfItems) {
              exec(pdpChain)
                .exec(addToBagChain())
            }
            .exec(removeAllItems())
        }
    }

    RemoveItems
  }

  def createAccount(rep:Int=1) = {
    val createAccount = repeat(rep) {
      exitBlockOnFail {
        exec(initialize)
          .exec(createAccountChain)
      }
    }

    createAccount
  }

  def registeredCheckout(numOfItems: Int =1, synthetic: Boolean = false) = {

    val SubmitOrder = if(synthetic) {
      exitBlockOnFail {
        exec(initialize)
          .asLongAs(session => session("numOfItems").as[Int] < numOfItems) {
            exec(pdpChain)
              .exec(addToBagChain())
          }
          .exec(RegCheckoutChain)
      }
    }
    else {
      during(duration) {
        pace((PDPPace + AddToBagPace) * numOfItems + BillingPace)
          .exitBlockOnFail {
            exec(initialize)
              .asLongAs(session => session("numOfItems").as[Int] < numOfItems) {
                exec(pdpChain)
                  .exec(addToBagChain())
              }
              .exec(RegCheckoutChain)
          }
      }
    }

    SubmitOrder
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