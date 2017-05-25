package server

import akka.actor.{Actor, Props}
import akka.actor._
import akka.pattern.ask
import akka.event.Logging

import scala.concurrent.duration._
import akka.util.Timeout

import scala.concurrent.Await

class FindActor extends Actor {
  val log = Logging(context.system, this)

  val find1: ActorRef = context.actorOf(Props(classOf[FindPriceActor], "prices1.txt"))
  val find2: ActorRef = context.actorOf(Props(classOf[FindPriceActor], "prices2.txt"))


  override def receive: Receive = {
    case rq: common.FindRequest => {
      implicit val timeout: Timeout = 1 seconds

      val future1 =  ask(find1 ,rq.name).mapTo[Option[Integer]]
      val future2 =  ask(find2 ,rq.name).mapTo[Option[Integer]]

      val result1 = Await.result(future1, Duration(1, "s"))
      val result2 = Await.result(future2, Duration(1, "s"))

      val result = if (result1.isDefined) result1 else result2

      sender() ! common.FindResponse(rq.name, result)
    }

    case _ => log.info("Received unknown message")
  }

}

