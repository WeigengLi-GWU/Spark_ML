package com.project.conf
import java.net._

object tracker {

  def main(args: Array[String]): Unit = {
    val res = track_ip()
    println(res)
  }
    def track_ip(): String = {
      var result = ""
      val e = NetworkInterface.getNetworkInterfaces
      var res: List[String] = List()
      while ( {
        e.hasMoreElements
      }) {
        val n = e.nextElement
        val ee = n.getInetAddresses
        while ( {
          ee.hasMoreElements
        }) {
          val i = ee.nextElement
          res = res :+ i.getHostAddress
        }
      }
      for (a <- res) {
        if (a.contains("192.168.1.")) {
          result = a
        }
      }
      return result
    }

}
