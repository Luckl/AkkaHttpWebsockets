package services

import actors.{PublisherActor, SubscriberActor}
import akka.actor.{Props, ActorSystem}
import akka.http.scaladsl.model.ws.{Message, TextMessage}

import akka.http.scaladsl.server.Directives
import akka.stream.Materializer
import akka.stream.scaladsl.{Source, Flow}
import scala.concurrent.duration._


class WebService(implicit fm: Materializer, system: ActorSystem) extends Directives {

  import system.dispatcher
  system.scheduler.schedule(15 second, 15 second) {
    println("Timer message!")
  }

  def route =
    get {
      pathSingleSlash {
        getFromResource("web/index.html")
      } ~
        path("helloworld") {
          handleWebsocketMessages(websocketActorFlow)
        }
    }

  def websocketActorFlow: Flow[Message, Message, Unit] =
    Flow[Message].collect({
      case TextMessage.Strict(msg) =>
        println(msg)
        TextMessage.Strict(msg.reverse)
    })
}


