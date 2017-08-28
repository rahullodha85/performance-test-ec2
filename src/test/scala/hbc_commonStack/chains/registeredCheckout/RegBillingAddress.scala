package hbc_commonStack.chains.registeredCheckout

import hbc_newLT.newLT_base.newLT
import hbc_commonStack.base.commonStack
import io.gatling.core.Predef._
import io.gatling.http.Predef._

/**
  * Created by aindana on 3/14/2016.
  */
object RegBillingAddress extends commonStack{
//  val random = new util.Random
//  val feeder = Iterator.continually(Map("email" -> (random.nextString(20) + "@foo.com")))

  lazy val chkUrl = getbasehttps() + "/checkout/checkout.jsp"

  lazy val submit_payment_service = "submit_payment_service"
  def Billing = group("BillingDetails_Transaction") {
    doIf(session => session("PaymentFlag").as[Int] != 0) {
        exec(http(submit_payment_service)
          .post(chkUrl)
          .body(StringBody("bmForm=submit_payment_service&CREDIT_CARD%3C%3EcardBrand_cd=VISA&CREDIT_CARD%3C%3EcardNum=4445222299990007&CREDIT_CARD%3C%3EcardholderName=Test&CREDIT_CARD%3C%3EcardMonth_cd=1&CREDIT_CARD%3C%3EcardYear_cd=2019&card_cvNumber=123&ACCOUNT%3C%3EaccountNumber=&ACCOUNT%3C%3EnotificationEmail=&promoCode="))
        //  .check(regex("""cardName":"${CardHolderName}""").exists)
          .check(regex("""grandTotal":"(\d+\.\d+)"""").saveAs("grandTotal"))
          .check(status.is(200))
        )


          .exec(session => {
            insertGroupResponseTimeToInfluxDB(session toString, "BillingAddress")
            insertGroupResponseTimeToNewRelic(session toString, "BillingAddress")
            session

          })

      }
  }



}
