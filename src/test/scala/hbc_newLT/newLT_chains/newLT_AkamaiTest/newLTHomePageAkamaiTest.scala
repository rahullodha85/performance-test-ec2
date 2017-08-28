package hbc_newLT.newLT_chains.newLT_AkamaiTest

import java.util.Random

import hbc_newLT.newLT_base.newLT
import io.gatling.core.Predef._
import io.gatling.http.Predef._

/**
  * Created by aindana on 3/14/2016.
  */
object newLTHomePageAkamaiTest extends newLT{

  def HomePage = group("HomePage_Transaction") {
    exec(http("HomePage")
      .get(getbasehttp())
           .check(currentLocation.saveAs("Url"))
      .check(status.is(200))
    )

        .exec(http("${Url}")
            .get("${Url}")
            .check(status.is(200))
        )

      .exec(session => {
        println(session("Url").as[String])
        session

      })

    }


  }
