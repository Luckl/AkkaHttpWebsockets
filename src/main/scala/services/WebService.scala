package services

import actors.{PublisherActor, SubscriberActor}
import akka.actor.{ActorLogging, Props, ActorSystem}
import akka.http.scaladsl.model.ws.{Message, TextMessage}

import akka.http.scaladsl.server.Directives
import akka.stream.{Outlet, Materializer}
import akka.stream.actor.ActorPublisher
import akka.stream.scaladsl.{Source, FlowGraph, Flow}

class WebService(implicit fm: Materializer, system: ActorSystem) extends Directives {

  val subscriber = system.actorOf(Props[SubscriberActor], "Subscriber")
  val publisher = system.actorOf(PublisherActor.props, "Publisher")

  def route =
    get {
      pathSingleSlash {
        getFromResource("web/index.html")
      } ~
        path("helloworld") {
          handleWebsocketMessages(websocketActorFlow)
        }
    } ~
      getFromResourceDirectory("web")


  /*
  Flow waar als source Messages uitkomen, en Messages terug verwacht worden.
  Waarschijnlijk de bedoeling dat deze flow terug met een publisherActor wordt gekoppeld waar berichten naar gestuurd
  kunnen worden
   */
  def websocketActorFlow: Flow[Message, Message, Unit] =
    Flow[Message, Message]() { implicit b =>

      val sendToActor = b.add(Flow[Message].map {
        case TextMessage.Strict(msg) => subscriber ! msg
      })

      val receiveFromActor = b.add(Source[Message](ActorPublisher.apply[Message](publisher)))

      (sendToActor.inlet, receiveFromActor)

    }
}


