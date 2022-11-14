package com.project.conf
import com.project.conf.tracker
import java.io._
class Auto_Hive_Conf {
    def write_master_nodes_bat():Unit={
      val ip = tracker.track_ip()
      val fileObject = new File("start_master.bat")
      val printWriter = new PrintWriter(fileObject)
      printWriter.write(
      "spark-class org.apache.spark.deploy.master.Master -h " + ip )
      printWriter.close() // Closing printwriter
    }

    def write_worker_nodes_bat(): Unit = {
      val ip = tracker.track_ip()
      val fileObject = new File("start_worker.bat")
      val printWriter = new PrintWriter(fileObject)
      printWriter.write(
        "spark-class org.apache.spark.deploy.worker.Worker spark://" + ip +":7077")
      printWriter.close() // Closing printwriter
    }
}

object Test {
   def main(args: Array[String]):Unit={
    var a= new Auto_Hive_Conf()
    a.write_master_nodes_bat()
    a.write_worker_nodes_bat()
   }

}