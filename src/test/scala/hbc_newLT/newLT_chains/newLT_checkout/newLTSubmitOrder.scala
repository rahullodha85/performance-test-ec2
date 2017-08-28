package hbc_newLT.newLT_chains.newLT_checkout


import hbc_newLT.newLT_base.newLT
import io.gatling.core.Predef._
import io.gatling.http.Predef._

import scalaj.http.Http

/**
  * Created by aindana on 3/14/2016.
  */
object newLTSubmitOrder extends newLT{

  lazy val submit_order_service = "submit_order_service"
  lazy val chkUrl = getbasehttps() + "/checkout/checkout.jsp"

  def SubmitOrder = group("SubmitOrder_Transaction") {
    exec(http(submit_order_service)
      .post(chkUrl)
      .body(StringBody("bmForm=submit_order_service&&"))
      .check(regex("""orderNumber":"(\d+)""").find.saveAs("OrderNumber"))
      .check(status.is(200))

      )

        .exec(session => {
        println(session("OrderNumber").as[String])
        insertGroupResponseTimeToInfluxDB(session toString, "SubmitOrder")
        insertGroupResponseTimeToNewRelic(session toString, "SubmitOrder")
        session

      })

    }


}
