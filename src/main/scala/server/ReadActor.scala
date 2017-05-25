package server

import akka.actor.Actor
import akka.event.Logging

class ReadActor extends Actor {
  val log = Logging(context.system, this)


  override def receive: Receive = {
    case _ => log.info("received unknown message")
  }
}

