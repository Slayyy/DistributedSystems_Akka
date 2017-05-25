package server

import akka.actor.{ActorSystem, Props}

import com.typesafe.config.ConfigFactory

object Server extends App {
  val system = ActorSystem("server", ConfigFactory.load("server.conf"))

  val find = system.actorOf(Props[FindActor], "find")
  val order = system.actorOf(Props[OrderActor], "order")
  val read = system.actorOf(Props[ReadActor], "read")

  println("----------SERVER READY----------")
}
