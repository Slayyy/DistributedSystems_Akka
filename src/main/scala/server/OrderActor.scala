package server

import java.io.{File, FileOutputStream, PrintWriter}

import akka.actor.{Actor, Props}
import akka.event.Logging
import akka.pattern.ask
import akka.util.Timeout

import scala.concurrent.duration._
import scala.concurrent.Await
import scala.concurrent.duration.Duration

class OrderActor extends Actor {
  val log = Logging(context.system, this)


  override def receive: Receive = {
    case rq: common.OrderRequest => {
      implicit val timeout: Timeout = 1 seconds

      val find = context.actorOf(Props[FindActor])
      val future = ask(find, common.FindRequest(rq.name)).mapTo[common.FindResponse]
      val result = Await.result(future, Duration(1, "s"))

      if (result.price.isEmpty) {
        sender() ! common.OrderResponse("not found")
      } else {
        classOf[OrderActor].synchronized {
          val path = getClass.getResource("/orders.txt").getPath
          val file = new PrintWriter(new FileOutputStream(new File(path), true))
          file.append(sender().toString()).append("\t").append(rq.name).append("\n")
          file.flush()
          file.close()
          sender() ! common.OrderResponse(rq.name + ": ordered")
        }
      }
    }
    case _ => log.info("Received unknown message")
  }
}

