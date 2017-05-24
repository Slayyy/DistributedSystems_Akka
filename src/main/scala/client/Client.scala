package client

import scala.io

object Client {
  def main(args: Array[String]): Unit = {
    for (ln <- io.Source.stdin.getLines) {
      println(ln)
    }
  }
}
