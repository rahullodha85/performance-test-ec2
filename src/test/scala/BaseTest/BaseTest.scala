package BaseTest

import java.nio.file.Path
import java.util.Date
import com.redis.RedisClient
import org.apache.http.client.methods.HttpOptions

import scala.collection.JavaConversions._

import java.io.File
import com.typesafe.config.{ Config, ConfigFactory }
import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.collection.mutable.ArrayBuffer
import scala.concurrent.duration._
import scala.xml.XML
import org.scalatest.FlatSpec

import scalaj.http._
import scalaj.http.HttpOptions

class BaseTest extends Simulation {

 // val baseUrl = "www.google.com"
  val baseUrl = System.getProperty("BaseUrl").toString
  lazy val instance = ""

  lazy val span =""

  lazy val baseUrlHttp = "http://" + baseUrl
  lazy val baseUrlHttps = "https://" + baseUrl


  lazy val uri3 = baseUrlHttps + "/account/login.jsp"
  lazy val uri4 = baseUrlHttps + "/account/registration.jsp"

  lazy val firstName ="Performance"
  lazy val lastName = "Script"
  lazy val password = "Password123"
  lazy val ProductsPA = jsonFile("src/test/resources/data/ProductsPA.json").circular
  lazy val Links = jsonFile("src/test/resources/data/Links.json").random
  lazy val saksO5Links = jsonFile("src/test/resources/data/saksoff5Links.json").random
  lazy val saksO5LinksProd = jsonFile("src/test/resources/data/saksoff5LinksProd.json").random
  lazy val ItemIDs = jsonFile("src/test/resources/data/ItemIDs.json").random
  lazy val SingleMetric = jsonFile("src/test/resources/data/SingleMetric.json").circular
  lazy val SearchString = jsonFile("src/test/resources/data/SearchString.json").random
  lazy val SearchStringS05 = jsonFile("src/test/resources/data/hbc_saksO5/SearchString.json").random
  lazy val BayLinks = jsonFile("src/test/resources/data/BayLinks.json").random
  lazy val LTLinks = jsonFile("src/test/resources/data/LTLinks.json").random
//  val ProductID = jsonFile("src/test/resources/data/ProductID.json").circular
  lazy val ShippingDetails = jsonFile("src/test/resources/data/ShippingDetails.json").circular
  lazy val BillingDetails = jsonFile("src/test/resources/data/BillingDetails.json").circular

  lazy val BAYDetails = jsonFile("src/test/resources/data/BAY/BAYShippingBillingDetails.json").circular

  lazy val Products = jsonFile("src/test/resources/data/Products.json").circular




  // Place Orders

  lazy val ShippingDetailsPO = jsonFile("src/test/resources/data/PlaceOrdersData/ShippingDetails.json").circular
  lazy val BillingDetailsPO = jsonFile("src/test/resources/data/PlaceOrdersData/BillingDetails.json").circular
  lazy val DCProducts = jsonFile("src/test/resources/data/PlaceOrdersData/DCProducts.json").circular
  lazy val StoreProducts = jsonFile("src/test/resources/data/PlaceOrdersData/StoreProducts.json").circular
  lazy val DCStoreProducts = jsonFile("src/test/resources/data/PlaceOrdersData/DCStoreProducts.json").circular
  //var email = ""

  //Session variables

 /* var bmUID =
  var bmFormID = ""*/

   var errorSet = scala.collection.immutable.HashSet("")

  lazy val httpConfChrome = http
    // .baseURL("http://www.qa.saks.com/") // Here is the root for all relative URLs
    .acceptHeader("application/json,text/javascript,text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8") // Here are the common headers
    .acceptLanguageHeader("en-US,en;q=0.5")
    .header("LoadTesting","Gatling")
    .header("Cookie","PT=Gatling")
    .acceptEncodingHeader("gzip, deflate")
    .userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:16.0) Gecko/20100101 Firefox/16.0")
    .inferHtmlResources(BlackList("""http:\/\/s7d9\.scene7\.com\/is\/image\/TheBay\/534_Footer_Brand_Logo_\S+""",""".*h\.online-etrix\.net.*""",""".*\.cookie.utils.js""",""".*\$SWATCHSMALL\$"""))
    .maxConnectionsPerHostLikeChrome
  //  .silentResources

  lazy val httpConfFireFox = http
    // .baseURL("http://www.qa.saks.com/") // Here is the root for all relative URLs
    .acceptHeader("application/json,text/javascript,text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8") // Here are the common headers
    .acceptLanguageHeader("en-US,en;q=0.5")
    .header("LoadTesting","Gatling")
    .header("Cookie","PT=Gatling")
    .acceptEncodingHeader("gzip, deflate")
    .userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:16.0) Gecko/20100101 Firefox/16.0")
    .inferHtmlResources(BlackList("""http:\/\/s7d9\.scene7\.com\/is\/image\/TheBay\/534_Footer_Brand_Logo_\S+""",""".*h\.online-etrix\.net.*""",""".*\.cookie.utils.js""",""".*\$SWATCHSMALL\$"""))
    .maxConnectionsPerHostLikeFirefox
  //  .silentResources



  lazy val httpConf = http
    // .baseURL("http://www.qa.saks.com/") // Here is the root for all relative URLs
    .acceptHeader("*/*")
    .acceptLanguageHeader("en-US,en;q=0.5")
    .connection("keep-alive")
    .header("LoadTesting","Gatling")
    .header("Cookie","PT=Gatling")
    .acceptEncodingHeader("gzip, deflate")
    .userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:16.0) Gecko/20100101 Firefox/16.0")
    .inferHtmlResources(BlackList("""http:\/\/s7d9\.scene7\.com\/is\/image\/TheBay\/534_Footer_Brand_Logo_\S+""",""".*h\.online-etrix\.net.*""",""".*\.cookie.utils.js""",""".*\$SWATCHSMALL\$"""))
     .silentResources
    .disableCaching
    .disableClientSharing
    .maxConnectionsPerHostLikeChrome

  lazy val httpConfDisableRedirect = http
    // .baseURL("http://www.qa.saks.com/") // Here is the root for all relative URLs
    .acceptHeader("*/*")
    .acceptLanguageHeader("en-US,en;q=0.5")
    .connection("keep-alive")
    .header("LoadTesting","Gatling")
    .header("Cookie","PT=Gatling")
    .acceptEncodingHeader("gzip, deflate")
    .userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:16.0) Gecko/20100101 Firefox/16.0")
    .inferHtmlResources(BlackList("""http:\/\/s7d9\.scene7\.com\/is\/image\/TheBay\/534_Footer_Brand_Logo_\S+""",""".*h\.online-etrix\.net.*""",""".*\.cookie.utils.js""",""".*\$SWATCHSMALL\$"""))
    .silentResources
    .disableCaching
    .disableClientSharing
    .maxConnectionsPerHostLikeChrome
      .disableFollowRedirect
    /*  .extraInfoExtractor {
        extraInfo =>
          extraInfo.status match {
            case io.gatling.core.result.message.KO => {
              var errorLabel = extraInfo.requestName + "-" + extraInfo.response.statusCode.getOrElse("501")
              if (!errorSet.contains(errorLabel)) {
                errorSet += errorLabel
                println("Error Label: " + errorLabel)
                println("Response Body" + extraInfo.response.body.string)
              }
            }
              Nil
            case _ => Nil
          }
      }*/

  //val address1 = java.net.InetAddress.getByName("202.168.223.60")
  lazy val httpConf1 = http
    // .baseURL("http://www.qa.saks.com/") // Here is the root for all relative URLs
    .acceptHeader("application/json,text/javascript,text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8") // Here are the common headers
    .acceptLanguageHeader("en-US,en;q=0.5")
    .header("LoadTesting","Gatling")
    .acceptEncodingHeader("gzip, deflate")
    .userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:16.0) Gecko/20100101 Firefox/16.0")
    .inferHtmlResources(BlackList("""http:\/\/s7d9\.scene7\.com\/is\/image\/TheBay\/534_Footer_Brand_Logo_\S+""",""".*h\.online-etrix\.net.*""",""".*\.cookie.utils.js""",""".*\$SWATCHSMALL\$"""))
    //.silentResources





  def generateRandomEmail() : String = {

    val timestamp: Long = System.currentTimeMillis
    val cremail = "test" + timestamp + "@script.com"
    cremail
  }

  def insertGroupResponseTime(start:Long, end:Long, transaction:String)  {


  }

  def insertGroupResponseTime(session:String, transaction:String)  {
    val pattern = "GroupBlock\\(List\\(\\S+\\),\\d+,(\\d+)".r


    val diff: String = (pattern findAllIn session) mkString(",") split(",") last

    try {




    var json: String = transaction + " value=" + diff

    val result = Http("http://localhost:8086/write")
      .param("db","performance")
      .postData(json)
      //.header("Content-Type", "application/json")
      .header("Charset", "UTF-8").asString
    }
    catch {

      case e: Exception =>
        println(e)
      //  System.exit(0)
    }


    try{

      //  val diff: String = (pattern findAllIn session).mkString(",").split(",").last

      var json2: String = "[{\"eventType\":\"GATLING\",\""+ transaction +"\":"+ diff +"}]"

      val result2 = Http("https://insights-collector.newrelic.com/v1/accounts/2000/events")
        .postData(json2)
        .header("Content-Type", "application/json")
        .header("X-Insert-Key", "pSDlJwOOeDWYzqo4X2bj3Xi-_GZoct5s").asString

    }
    catch {

      case e: Exception =>
        println(e)
      //  System.exit(0)
    }



  }


  def normalizeUsers(Users:Int) : Int = {
    var newUsers: Int = Users
    if(Users < 1) newUsers = 1
    newUsers
  }

  /*def readXml(tagName:String, path:String) : String = {

    val data = XML.loadFile("repo/homePageRepo.xml");

  }*/




}
