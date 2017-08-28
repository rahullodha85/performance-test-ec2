package hbc_newLT.newLT_chains.newLT_checkout


import hbc_newLT.newLT_base.newLT
import io.gatling.core.Predef._
import io.gatling.http.Predef._

/**
  * Created by aindana on 3/14/2016.
  */
object newLTShippingAddress extends newLT{

  lazy val validate_ship_address_service ="validate_ship_address_service"
  lazy val add_address_and_continue_service = "add_address_and_continue_service"
  lazy val creditCardTypes = "bmForm=get_option_list_service&listName=creditCardTypes"

  lazy val chkUrl = getbasehttps() + "/checkout/checkout.jsp"

  def Shipping = group("ShippingAddress_Transaction") {
    feed(ShippingDetails)
      .exec(http(validate_ship_address_service)
      .post(chkUrl)
         .body(StringBody("bmForm=validate_ship_address_service&shiptomult=false&SHIP_TO_ADDRESS<>indGift=${ChangeItemQty}&SHIP_TO_ADDRESS<>firstName=${FirstName}&SHIP_TO_ADDRESS<>middleName=&SHIP_TO_ADDRESS<>lastName=${LastName}&SHIP_TO_ADDRESS<>address3=${Company}&SHIP_TO_ADDRESS<>address1=${Address}&SHIP_TO_ADDRESS<>address2=&SHIP_TO_ADDRESS<>city=${City}&SHIP_TO_ADDRESS<>state_cd=${State}&SHIP_TO_ADDRESS<>postal=${Zip}&setAsBillAddress=true&LOGIN<>userid=${Email}&SHIP_TO_ADDRESS<>phone=${Phone}&count=1&SHIP_TO_ADDRESS<>uad_id="))
     //   .check(bodyString.saveAs("ShippingAddress1"))
        .check(status.is(200))
      )



      .exec(http(add_address_and_continue_service)
        .post(chkUrl)
        .formParam("bmForm","add_address_and_continue_service")
        .formParam("shiptomult","false")
        .formParam("SHIP_TO_ADDRESS<>indGift","${ChangeItemQty}")
        .formParam("SHIP_TO_ADDRESS<>firstName","${FirstName}")
        .formParam("SHIP_TO_ADDRESS<>middleName","")
        .formParam("SHIP_TO_ADDRESS<>lastName","${LastName}")
        .formParam("SHIP_TO_ADDRESS<>address3","${Company}")
        .formParam("SHIP_TO_ADDRESS<>address1","${Address}")
        .formParam("SHIP_TO_ADDRESS<>address2","")
        .formParam("SHIP_TO_ADDRESS<>city","${City}")
        .formParam("SHIP_TO_ADDRESS<>state_cd","${State}")
        .formParam("SHIP_TO_ADDRESS<>postal","${Zip}")
        .formParam("setAsBillAddress","true")
        .formParam("LOGIN<>userid","${Email}")
        .formParam("SHIP_TO_ADDRESS<>phone","${Phone}")
        .formParam("count","1")
        .formParam("SHIP_TO_ADDRESS<>uad_id","")
        .formParam("BILL_TO_ADDRESS<>state_cd","${State}")
        .check(bodyString.saveAs("ShippingAddress2"))
        .check(regex("""address1":"${Address}""").exists)
       // .check(bodyString.saveAs("ShippingAddress2"))
        .check(status.is(200))

      )

      .exec(http(creditCardTypes)
        .post(chkUrl)
        //.body(StringBody("bmForm=get_option_list_service&listName=creditCardTypes"))
        .body(StringBody("bmForm=get_option_list_service&listName=titles"))//bmForm=get_option_list_service&listName=titles
          .check(regex("""display":"Select""").exists)
        .check(status.is(200))
        //.check(bodyString.saveAs("ShippingAddress3"))
      )

      .exec(session => {
        insertGroupResponseTimeToInfluxDB(session toString, "ShippingAddress")
        insertGroupResponseTimeToNewRelic(session toString, "ShippingAddress")
        session

      })




  }



}
