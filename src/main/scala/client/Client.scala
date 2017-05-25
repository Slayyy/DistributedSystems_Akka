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
    case rq: common.FindRequest => tellServerActor("find", rq)
    case rq: common.OrderRequest => tellServerActor("order", rq)
    case rq: common.ReadRequest => tellServerActor("read", rq)

    case res: common.FindResponse => println(res.price match {
      case Some(value) => println("%s %s".format(res.name, value.toString))
      case None => println("%s not found".format(res.name))
    })
    case res: common.OrderResponse => println(res.result)

    case _ => log.info("Received unknown message")
  }
}


object Client extends App {
  val system = ActorSystem("client", ConfigFactory.load("client.conf"))

  val client = system.actorOf(Props[ClientActor], "client")

  println("----------CLIENT READY----------")
  io.Source.stdin.getLines.foreach((line) =>
      line.split(" ", 2) match {
        case Array("find", args) => client ! common.FindRequest(args)
        case Array("order", args) => client ! common.OrderRequest(args)
        case Array("read", args) => client ! common.ReadRequest(args)
        case _ => println(s"Bad arg: $line")
      }
  )

}
