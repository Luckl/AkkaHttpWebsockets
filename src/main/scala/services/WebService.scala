package services

import akka.actor.ActorSystem
import akka.http.scaladsl.model.ws.{Message, TextMessage}

import akka.http.scaladsl.server.Directives
import akka.stream.Materializer

class WebService(implicit fm: Materializer, system: ActorSystem) extends Directives {

  def route =
    get {
      pathSingleSlash {
        getFromResource("web/index.html")
      }
    } ~
      getFromResourceDirectory("web")
}

