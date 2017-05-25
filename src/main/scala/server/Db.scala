package server

import akka.actor.Actor
import akka.event.Logging

import scala.io
import akka.pattern.ask


class FindPriceActor(val filePath: String) extends Actor {
  val log = Logging(context.system, this)

  def findPriceInFile(filePath: String)(title: String): Option[Integer] =
      io.Source.fromResource(filePath).getLines()
        .map((line) => line.split(" "))
        .find((s) => s.length >= 2 && s(0) == title)
        .map((s) => s(1).toInt)

  override def receive: Receive = {
    case name: String => context.sender() ! findPriceInFile(filePath)(name)
    case _ => log.info("received unknown message")
  }
}

