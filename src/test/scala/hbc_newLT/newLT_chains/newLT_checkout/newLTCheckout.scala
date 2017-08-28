package hbc_newLT.newLT_chains.newLT_checkout


import hbc_newLT.newLT_base.newLT
import io.gatling.core.Predef._
import io.gatling.http.Predef._

/**
  * Created by aindana on 3/14/2016.
  */
object newLTCheckout extends newLT{
//  val random = new util.Random
//  val feeder = Iterator.continually(Map("email" -> (random.nextString(20) + "@foo.com")))

  lazy val chkUrl = getbasehttps() + "/checkout/checkout.jsp"
  lazy val login_as_guest_user = "login_as_guest_user"
  lazy val Get_Checkout = "Get_Checkout"
  lazy val continue_to_checkout_service = "continue_to_checkout_service"
  lazy val titles = "get_option_list_service&listName=titles"
  lazy val states = "get_option_list_service&listName=states"
  def Checkout = group("GuestCheckout_${numOfItems}Items_Transaction") {
    exec(http(login_as_guest_user)
      .post(chkUrl)
        .body(StringBody("bmForm=login_as_guest_user&LOGIN<>userid="))
      .check(status.is(200))

     //   .check(regex("""soldOut":false""").exists)

      )

      .exec(http(Get_Checkout)
      .get(chkUrl)
      //.check(regex("""${ProductID}""").exists)
        .check(status.is(200))
    )

      .exec(http(continue_to_checkout_service)
        .post(chkUrl)
        .body(StringBody("bmForm=continue_to_checkout_service"))
          .check(status.is(200))
      )
      .exec(http(titles)
        .post(chkUrl)
        .body(StringBody("bmForm=get_option_list_service&listName=titles"))
        .check(regex("""Select a Title""").exists)
        .check(status.is(200))
      )
      .exec(http(states)
        .post(chkUrl)
        .body(StringBody("bmForm=get_option_list_service&listName=states"))
        .check(regex("""Choose a State""").exists)
        .check(status.is(200))
      )
      .exec(session => {
        insertGroupResponseTimeToInfluxDB(session toString, "Checkout_" + session("numOfItems").as[String] + "Items")
        insertGroupResponseTimeToNewRelic(session toString, "Checkout_" + session("numOfItems").as[String] + "Items")
        session

      })


  }



}
