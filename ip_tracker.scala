import java.net._
object ip_tracker {
  def main(args: Array[String]) = {
    val localhost: InetAddress = InetAddress.getLocalHost
    val localIpAddress: String = localhost.getHostAddress
    println(s"localIpAddress = $localIpAddress")
  }
}