import akka.actor.ActorSystem
import org.mashupbots.socko.routes._
import org.mashupbots.socko.webserver.{WebServer, WebServerConfig}

object Application extends App {

  val system = ActorSystem("System")


  val routes = Routes({
    case HttpRequest(httpRequest) => httpRequest match {
      case Path("/helloworld/") => {
        httpRequest.response.write("Sup son")
    }
  }
    case WebSocketHandshake(wsHandshake) => wsHandshake match {
      case Path("/helloworld/") => {
        wsHandshake.authorize()
      }
      case WebSocketFrame(wsFrame) => {
        wsFrame.writeText("Hello World!")
      }
    }
  })

  val webServer = new WebServer(WebServerConfig(), routes, system)
  webServer.start()
}
