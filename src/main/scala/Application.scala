import akka.actor._
import akka.http.Http
import akka.http.server.Directives._
import akka.stream.ActorFlowMaterializer
import akka.stream.scaladsl.Flow

object Application extends App {

  implicit val system = ActorSystem("api")
  implicit val materializer = ActorFlowMaterializer()
  implicit val executor = system.dispatcher

  val route =
    get {
      pathSingleSlash {
        getFromResource("index.html")
      }
    } ~
      getFromResourceDirectory("")

  val bindingFuture = Http().bindAndHandle(route, "localhost", 8080)
}
