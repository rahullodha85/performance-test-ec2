package hbc_loadTests

import com.redis.RedisClient
import com.typesafe.config.ConfigFactory
import io.gatling.core.Predef._
import io.gatling.core.config.Protocol
import org.mongodb.scala._

import scalaj.http.Http
import hbc_loadTests.helpers.Helpers._

class LoadTest extends loadTestingSettings{

  def InfluxDB(banner:String)(session:String, transaction:String): Option[String] =
  {
    try {
      if(influxDB) {
        val host = if(local) "hd1qjra03lx.digital.hbc.com" else "localhost"
        val pattern = "GroupBlock\\(List\\(\\S+\\),\\d+,(\\d+)".r
        val diff: String = (pattern findAllIn session) mkString (",") split (",") last

        var json: String = banner + transaction + " value=" + diff
        val result = Http("http://"+host+":8086/write")
          .param("db", "performance")
          .postData(json)
          .header("Charset", "UTF-8").asString

      }
      Some("Working")
    }
    catch {
      case e: Exception => {
        println(e)
      }
        None
    }

  }

  def NewRelic(banner:String)(session:String, transaction:String): Option[String] = {
    try {
      if (newRelic) {

        val pattern = "GroupBlock\\(List\\(\\S+\\),\\d+,(\\d+)".r
        val diff: String = (pattern findAllIn session) mkString (",") split (",") last
        var json2: String = "[{\"eventType\":\"GATLING\",\"" + banner + "_" + transaction + "\":" + diff + "}]"
        val result2 = Http("https://insights-collector.newrelic.com/v1/accounts/2000/events")
          .postData(json2)
          .header("Content-Type", "application/json")
          .header("X-Insert-Key", "pSDlJwOOeDWYzqo4X2bj3Xi-_GZoct5s").asString
      }

      Some("Working")
    }
    catch {
      case e: Exception => {
        println(e)
      }
        None
    }


  }


  def getbasehttps(): String = {
    "https://" + baseUrl
  }

  def getbasehttp(): String = {
    "http://" + baseUrl
  }


  def insertAccountData(db: String, col: String, username: String, password: String): Unit ={
    val mongoClient: MongoClient = MongoClient(mongoUrl)

    val database: MongoDatabase = mongoClient.getDatabase(db)

    val collection: MongoCollection[Document] = database.getCollection(col)

    val doc: Document = Document("username" -> username, "password" -> password)

    collection.insertOne(doc).results()

    mongoClient.close()
  }



 /* //val r = new RedisClient("10.0.0.213", 6379)
//  val r = new RedisClient("hd1qjra03lx.digital.hbc.com", 6379)

  def incrementer(increment: String): Int = {
    val inc = show(r.lpop(increment)).toInt + 1
    r.lpush(increment, inc)
    inc
  }

  def readInc(increment: String): Int = {
    val inc = show(r.lpop(increment)).toInt
    r.lpush(increment, inc)
    inc
  }


  def iniIncrementer(increment: String) = {
    r.set(increment,1)
  }

  def setKey[A,B](ListName: A, ListValue: B) = {
    r.set(ListName, ListValue)
  }

  def getKey(increment: String): Int = {
    var inc =0
    try {
       inc = show(r.get(increment)).toInt
    }
    catch {
      case e: Exception =>
        Thread.sleep(10000)
        inc = show(r.get(increment)).toInt
    }
    inc
  }

  def incKey(increment: String) = {
    r.incr(increment)

  }

  def inputRedis[A,B](ListName: A, ListValue: B) = {
      r.sadd(ListName, ListValue)
  }

  def inputRedis[A,B](ListName: A, ListValue: List[B]) = {
    ListValue.foreach(r.sadd(ListName, _))
  }

  def show(x: Option[String]) = x match {
    case Some(s) => s
    case None => "?"
  }

  def readRandRedis[A,B](ListName: A): String = {
      show(r.srandmember(ListName))
  }

  def delRedis[A,B](ListName: A) = {
    try {
      r.del(ListName)
    }
    catch {

      case e: Exception =>
        println(e)
    }
  }*/

  def normalizeUsers(Users:Int) : Int = {
    var newUsers: Int = Users
    if(Users < 1) newUsers = 1
    newUsers
  }


}


trait loadTestingSettings {
  implicit def double2int(users:Double) = users.toInt
  implicit def String2Boolean(toggle:String) = toggle.toBoolean
  lazy val silentRes: Boolean= if(System.getProperty("SilentResources").contains("true")) true else false
  lazy val disableCache: Boolean = if(System.getProperty("DisableCaching").contains("true")) true else false
  lazy val baseUrl = System.getProperty("BaseUrl").toString
  lazy val mongoUrl = "mongodb://10.32.150.38"
  lazy val influxDB: Boolean = if(System.getProperty("InfluxDB").contains("true")) true else false
  lazy val local: Boolean = if(System.getProperty("Local").contains("true")) true else false
  lazy val newRelic: Boolean = if(System.getProperty("NewRelic").contains("true")) true else false
}


