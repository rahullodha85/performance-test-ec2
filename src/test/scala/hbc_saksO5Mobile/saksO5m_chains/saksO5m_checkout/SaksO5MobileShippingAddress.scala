package hbc_saksO5Mobile.saksO5m_chains.saksO5m_checkout

import BaseTest.BaseTest
import hbc_saksO5Mobile.saksO5m_base.SaksO5Mobile
import io.gatling.core.Predef._
import io.gatling.http.Predef._

/**
  * Created by aindana on 3/14/2016.
  */
object SaksO5MobileShippingAddress extends SaksO5Mobile{
  //  val random = new util.Random
  //  val feeder = Iterator.continually(Map("email" -> (random.nextString(20) + "@foo.com")))

  lazy val chkUrl = getbasehttps() + "/checkout/checkout.jsp"

  def Shipping = group("ShippingAddress_Transaction") {
    feed(ShippingDetails)
      .exec(http("validate_ship_address_service")
      .post(chkUrl)
    .body(StringBody("bmForm=validate_ship_address_service&shiptomult=false&SHIP_TO_ADDRESS<>indGift=${ChangeItemQty}&SHIP_TO_ADDRESS<>firstName=${FirstName}&SHIP_TO_ADDRESS<>middleName=&SHIP_TO_ADDRESS<>lastName=${LastName}&SHIP_TO_ADDRESS<>address3=${Company}&SHIP_TO_ADDRESS<>country_name=United%20States&SHIP_TO_ADDRESS<>country_cd=US&SHIP_TO_ADDRESS<>address1=${Address}&SHIP_TO_ADDRESS<>address2=&SHIP_TO_ADDRESS<>city=${City}&SHIP_TO_ADDRESS<>state_cd=${State}&SHIP_TO_ADDRESS<>postal=${Zip}&setAsBillAddress=true&LOGIN<>userid=${Email}&SHIP_TO_ADDRESS<>phone=${Phone}&count=1&SHIP_TO_ADDRESS<>uad_id="))
    //  .check(bodyString.saveAs("ShippingAddress1"))
      .check(status.is(200))
    )



      .exec(http("add_address_and_continue_service")
        .post(chkUrl)
     .body(StringBody("bmForm=add_address_and_continue_service&SHIP_TO_ADDRESS<>uad_id=&SHIP_TO_ADDRESS<>firstName=${FirstName}&SHIP_TO_ADDRESS<>lastName=${LastName}&SHIP_TO_ADDRESS<>address3=${Company}&SHIP_TO_ADDRESS<>country_name=United%20States&SHIP_TO_ADDRESS<>country_cd=US&SHIP_TO_ADDRESS<>address1=${Address}&SHIP_TO_ADDRESS<>address2=&SHIP_TO_ADDRESS<>city=${City}&SHIP_TO_ADDRESS<>postal=${Zip}&SHIP_TO_ADDRESS<>state_cd=${State}&setAsBillAddress=true&LOGIN<>userid=${Email}&SHIP_TO_ADDRESS<>phone=${Phone}&count=1&"))
        .check(regex("""address1":"${Address}""").exists)
      //  .check(bodyString.saveAs("ShippingAddress2"))
        .check(status.is(200))

      )

      .exec(http("bmForm=get_option_list_service&listName=creditCardTypes")
        .post(chkUrl)
        .body(StringBody("bmForm=get_option_list_service&listName=creditCardTypes"))
        .check(regex("""display":"Select Payment Type""").exists)
        .check(status.is(200))
      //  .check(bodyString.saveAs("ShippingAddress3"))
      )

      .exec(session => {
        insertGroupResponseTimeToInfluxDB(session toString, "ShippingAddress")
        insertGroupResponseTimeToNewRelic(session toString, "ShippingAddress")

        session

      })



  }



}
