package actors

import actors.SubscriberActor.Msg
import akka.actor.{Actor, ActorLogging}

object SubscriberActor {
  case class Msg(message: String)
}

class SubscriberActor extends Actor with ActorLogging{

  def handleMessage(message: String): Unit = {
    log.debug(message)
  }

  override def receive: Actor.Receive = {
    case Msg(message) => handleMessage(message)
  }
}
