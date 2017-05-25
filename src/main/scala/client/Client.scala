package client

import akka.actor.{Actor, ActorSystem, Props}
import akka.event.Logging
import com.typesafe.config.ConfigFactory

import scala.io



class ClientActor extends Actor {
  val log = Logging(context.system, this)

  val ServerPath = "akka.tcp://server@127.0.0.1:22222/user"

  def resolveServerActorPath(actorName: String): String = s"$ServerPath/$actorName"

  def tellServerActor(actorName: String, msg: Any): Unit =
    context.actorSelection(resolveServerActorPath(actorName)).tell(msg, context.self)

  override def receive: Receive = {
    case rq: common.SearchRequest => tellServerActor("search", rq)
    case rq: common.OrderRequest => tellServerActor("order", rq)
    case rq: common.ReadRequest => tellServerActor("read", rq)
    case _ => log.info("Received unknown message")
  }
}


object Client extends App {
  val system = ActorSystem("client", ConfigFactory.load("client.conf"))

  val client = system.actorOf(Props[ClientActor], "client")

  println("----------CLIENT READY----------")
  for (line <- io.Source.stdin.getLines) {
      line.split(" ", 2) match {
        case Array("search", args) => client ! common.SearchRequest(args)
        case Array("order", args) => client ! common.OrderRequest(args)
        case Array("read", args) => client ! common.ReadRequest(args)
        case _ => println(s"Bad arg: $line")
      }
  }
}
