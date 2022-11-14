import java.net._
object ip_tracker {
  def main(args: Array[String]) = {
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
    for(a <- res) {
      if (a.contains("192.168.1.")){
        println(a);
      }
    }
  }
}