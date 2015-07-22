import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.ws.{TextMessage, Message}
import akka.stream.ActorMaterializer
import akka.http.scaladsl.server.Directives._
import akka.stream.scaladsl.Flow
import services.WebService

import scala.concurrent.Await
import scala.concurrent.duration._
import java.util.concurrent.TimeoutException


object Application extends App {

  implicit val system = ActorSystem("api")
  implicit val materializer = ActorMaterializer()

  import system.dispatcher

  val config = system.settings.config
  val interface = config.getString("app.interface")
  val port = config.getInt("app.port")

  val service = new WebService

  val binding = Http().bindAndHandle(service.route, interface, port)

  try {
    Await.result(binding, 1 second)
    println(s"server online at http://$interface:$port/")
  } catch {
    case exc: TimeoutException =>
      println("Server took to long to startup, shutting down")
      system.shutdown()
  }
}
