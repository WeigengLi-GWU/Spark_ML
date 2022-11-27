package com.wic.gui
import com.wic.gui.tracker
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

    def write_worker_nodes_bat(master_ip: String): Unit = {
      val fileObject = new File("start_worker.bat")
      val printWriter = new PrintWriter(fileObject)
      printWriter.write(
        "spark-class org.apache.spark.deploy.worker.Worker spark://" + master_ip +":7077")
      printWriter.close() // Closing printwriter
    }

    def write_submit_nodes_bat(): Unit = {
      val ip = tracker.track_ip()
      val fileObject = new File("submit_job.bat")
      val classs = "org.apache.spark.examples.SparkPi"
      val master = "spark://192.168.1.14:7077"
      val deploy_mode = "cluster"
      val executor_memory = "20G"
      val total_executor_cores = "100"
      val printWriter = new PrintWriter(fileObject)
      printWriter.write(
        "./bin/spark-submit \n" +
        "--class " + classs + " \n"+
        "--master " + master + " \n"+
        "--deploy-mode " + deploy_mode + " \n"+
        "--supervise \n" +
        "--executor-memory " + executor_memory + " \n" +
        "--total-executor-cores " + total_executor_cores + " \n" +
        "/path/to/examples.jar \n" +
        "1000")
      printWriter.close()
    }
}

object Test {
   def main(args: Array[String]):Unit={
    var a= new Auto_Hive_Conf()
    val master_ip = tracker.track_ip()
    a.write_master_nodes_bat()
    a.write_worker_nodes_bat(master_ip)
    a.write_submit_nodes_bat()
   }

}