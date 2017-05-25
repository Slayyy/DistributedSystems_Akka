package server

import akka.actor.{ActorSystem, Props}

import com.typesafe.config.ConfigFactory

object Server extends App {
  val system = ActorSystem("server", ConfigFactory.load("server.conf"))

  val search = system.actorOf(Props[SearchActor], "search")

  println("----------SERVER READY----------")
}
