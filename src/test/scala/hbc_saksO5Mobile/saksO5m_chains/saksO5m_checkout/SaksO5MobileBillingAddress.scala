package hbc_saksO5Mobile.saksO5m_chains.saksO5m_checkout

import BaseTest.BaseTest
import hbc_saksO5Mobile.saksO5m_base.SaksO5Mobile
import io.gatling.core.Predef._
import io.gatling.http.Predef._

/**
  * Created by aindana on 3/14/2016.
  */
object SaksO5MobileBillingAddress extends SaksO5Mobile{
//  val random = new util.Random
//  val feeder = Iterator.continually(Map("email" -> (random.nextString(20) + "@foo.com")))

  lazy val chkUrl = getbasehttps() + "/checkout/checkout.jsp"

    def Billing = group("BillingDetails_Transaction") {
    feed(BillingDetails)

      .exec(http("submit_payment_service")
      .post(chkUrl)
          .body(StringBody("bmForm=submit_payment_service&CREDIT_CARD%3C%3EcardBrand_cd=VISA&CREDIT_CARD%3C%3EcardNum=4445222299990007&CREDIT_CARD%3C%3EcardholderName=Test&CREDIT_CARD%3C%3EcardMonth_cd=7&CREDIT_CARD%3C%3EcardYear_cd=2017&card_cvNumber=123&ACCOUNT%3C%3EaccountNumber=&ACCOUNT%3C%3EnotificationEmail=&promoCode=&USER_ACCOUNT%3C%3Epassword=&USER_ACCOUNT%3C%3EconfirmPassword=&USER_ACCOUNT%3C%3EATR_passwordHint="))
        //.check(bodyString.saveAs("BillingDetails"))
          .check(regex("""cardName":"${CardHolderName}""").exists)
        .check(status.is(200))
      )
      .exec(session => {

        insertGroupResponseTimeToInfluxDB(session toString, "BillingAddress")
        insertGroupResponseTimeToNewRelic(session toString, "BillingAddress")
        session

      })

    }



}
