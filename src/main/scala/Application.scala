import akka.actor.{ActorLogging, ActorSystem}
import org.mashupbots.socko.infrastructure.Logger
import org.mashupbots.socko.routes._
import org.mashupbots.socko.webserver.{WebServer, WebServerConfig}

object Application extends App with Logger{

  val system = ActorSystem("System")
  var calls = 0

  val routes = Routes({
    case HttpRequest(httpRequest) => httpRequest match {
      case Path("/helloworld/") => httpRequest.response.write("Sup son")
    }
    case WebSocketHandshake(wsHandshake) => wsHandshake match {
      case Path("/helloworld/") => wsHandshake.authorize()
    }
    case WebSocketFrame(wsFrame) =>
      calls += 1
      log.debug(s"We have received $calls calls")
      wsFrame.writeText(calls.toString)

  })

  val webServer = new WebServer(WebServerConfig(), routes, system)
  webServer.start()
}
