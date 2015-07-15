package actors

import akka.actor.{Props, ActorLogging}
import akka.http.scaladsl.model.ws.{TextMessage, Message}
import akka.stream.actor.ActorPublisher
import akka.stream.actor.ActorPublisherMessage.{Request, Cancel}

import scala.annotation.tailrec

object PublisherActor {
  def props: Props = Props[PublisherActor]
  case class PublishMessage(payload: String)
}

class PublisherActor extends ActorPublisher[Message] with ActorLogging{
  import actors.PublisherActor._

  val MaxBufferSize = 100
  var buf = Vector.empty[Message]

  override def receive: Receive = {
    case publishMessage: PublishMessage if buf.size == MaxBufferSize =>
      log.warning("Buffer max reached, potential cause of issues")
    case publishMessage: PublishMessage =>
      if (buf.isEmpty && totalDemand > 0)
        onNext(TextMessage.Strict(publishMessage.payload))
      else {
        buf :+= TextMessage.Strict(publishMessage.payload)
        deliverBuf()
      }
    case Request(_) =>
      deliverBuf()
    case Cancel =>
      context.stop(self)
  }


  @tailrec final def deliverBuf(): Unit =
    if (totalDemand > 0) {
      /*
       * totalDemand is a Long and could be larger than
       * what buf.splitAt can accept
       */
      if (totalDemand <= Int.MaxValue) {
        val (use, keep) = buf.splitAt(totalDemand.toInt)
        buf = keep
        use foreach onNext
      } else {
        val (use, keep) = buf.splitAt(Int.MaxValue)
        buf = keep
        use foreach onNext
        deliverBuf()
      }
    }
}

