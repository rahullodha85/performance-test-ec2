package hbc_commonStack.chains.ServiceLevelTests

import hbc_commonStack.base.commonStack
import io.gatling.core.Predef._
import io.gatling.http.Predef._

/**
  * Created by aindana on 3/14/2016.
  */
object saksO5ColorSwatchService extends commonStack{

  val url = getbasehttp() + "/v1/color-swatch-service/product-swatches?maxSwatches=11"
  def CSService = group("ColorSwatchService_Transaction") {
    exec(session => {
       val body = "{\"product_codes\":["

        session.set("body",body)
      })

        .repeat(60)
      {
        feed(Products)
          .exec(session => {
          val body =  session("body").as[String] + "\"" + session("ProductID").as[String] + "\","

          session.set("body",body)
        })
      }

      .exec(session => {
      val body = session("body").as[String].dropRight(1) + "]}"
     // println(body)
      session.set("body",body)
    })

      .exec(http("CS")
      .post(url)
        .check(status.is(200))
        .body(StringBody("${body}")).asJSON
        .check(regex("""product_swatch_info""").exists)
   //     .check(bodyString.saveAs("Response"))
    )

 /*     .exec(session => {
        println(session("Response").as[String])
        session
      })*/

      .exec(session => {
     // println("Test")
    /*  if(session("AddCount").as[Int]==0)
        {
          println("SESSION")
          println(session("Response").as[String])
        }*/
      insertGroupResponseTimeToInfluxDB(session toString, "ColorSwatchService")
      insertGroupResponseTimeToNewRelic(session toString, "ColorSwatchService")
      session

    })
  }


  }
