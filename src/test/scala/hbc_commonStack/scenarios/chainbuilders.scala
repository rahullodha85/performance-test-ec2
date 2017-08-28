package hbc_commonStack.scenarios

import hbc_commonStack.base._
import hbc_commonStack.chains.PARefinements.{FilterBy, PARefine}
import hbc_commonStack.chains.ServiceLevelTests.{genericGETRequest, saksO5ColorSwatchService, saksO5ProductDetailService}
import hbc_commonStack.chains.checkout._
import hbc_commonStack.chains.createAccount.{CreateAccount, SignOut}
import hbc_commonStack.chains.pa.PA
import hbc_commonStack.chains.pdp.{PDP, PDPPre}
import hbc_commonStack.chains.registeredCheckout.{RegBillingAddress, RegShippingAddress, RegisteredCheckout}
import hbc_commonStack.chains.searchText.TextSearch
import io.gatling.core.Predef._
import io.gatling.core.structure.ChainBuilder

import scala.language.postfixOps

/**
  * Created by aindana on 3/14/2016.
  */
object chainBuilders extends commonStack with LoadSettings with WLMSettings{

  def initialize = clearSession.clearSession exec setCookie.setCookieSaksO5
 // def initialize2 = clearSession.clearSession
  def PDPServiceChain = clearSession.clearSession exec saksO5ProductDetailService.PDPService

  def ColorSwatchServiceChain = clearSession.clearSession exec saksO5ColorSwatchService.CSService

  def createAccountChain = CreateAccount.CreateAccount exec SignOut.signOut


  def homePageChain = HomePage.HomePage

  def paChain(PALink: String = "None") = PA.PA(PALink)

  def pdpChain = asLongAs(session => session("sku_id").as[String].equals("0")) {
    exec(PDPPre.PDPPre)
      .exec(PDP.PDP)
  }

  def paRefineChain = doIf(session => session("baseRefinementUrl_count").as[Int] > 0) {
    exec(FilterBy.FilterBy)
      .exec(PARefine.PARefine)
  }

  def searchTermChain = TextSearch.TextSearch



  def billingChain = Checkout.Checkout exec ShippingAddress.Shipping exec BillingAddress.Billing

  def guestCheckoutChain= Checkout.Checkout exec ShippingAddress.Shipping exec BillingAddress.Billing

  // def RegCheckoutChain: ChainBuilder = newLTChangeQty.ChangeQty exec newLTShippingMethod.shippingMethod exec newLTRegisteredCheckout.Checkout exec newLTRegShippingAddress.Shipping exec newLTRegBillingAddress.Billing exec newLTSubmitOrder.SubmitOrder

  def RegCheckoutChain= RegisteredCheckout.Checkout exec RegShippingAddress.Shipping exec RegBillingAddress.Billing

  def removeAllItems() =  doIf(session => session("cartItemIDCount").as[Int] > 0) {
    exec(RemoveItem.removeItem)
  }


  def addToBagChain(ItemQty:String="1"): ChainBuilder = {
      exec(AddToBag.AddToBag(ItemQty))
        .exec(ShoppingBag.ShoppingBag)
  }

  def HomePage_() = {
    val HomePage = during(duration) {
      pace(HomePagePace)
        .exitBlockOnFail {
          exec(initialize)
            .exec(homePageChain)
        }
    }

    HomePage
  }

  def PARefine_(synthetic: Boolean = false) = {
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


  def PDP_() = {
    val PDP = during(duration) {
      pace(PDPPace)
        .exitBlockOnFail {
          exec(initialize)
            .exec(pdpChain)
        }
    }

    PDP
  }

  def AddToBag_(numOfItems: Int =1) = {
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



  def PDPServiceTest() = {
    val PDPService = during(duration) {
      pace(PDPServicePace)
        .exitBlockOnFail {
          exec(PDPServiceChain)
        }
    }

    PDPService
  }

  def ColorServiceTest() = {
    val CSService = during(duration) {
      pace(CSServicePace)
        .exitBlockOnFail {
          exec(ColorSwatchServiceChain)
        }
    }

    CSService
  }

  def genericGETRequestTest() = {
    val genericGET = during(duration) {
        exitBlockOnFail {
          exec(genericGETRequest.test)
        }
    }

    genericGET
  }

  def guestCheckout2(numOfItems: Int =1) = {

    val SO =
      during(duration) {
        pace(60)
          .tryMax(3) {
             exitBlockOnFail {
              exec(initialize)
                .exec(pdpChain)
                .exec(AddToBag.AddToBag())
                .exec(ShoppingBag.ShoppingBag)
                .exec(Checkout.Checkout)
                .exec(ShippingAddress.Shipping)
                .exec(BillingAddress.Billing)
                .exec(SubmitOrder.SubmitOrder)
            }
          }
      }


    SO
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


}