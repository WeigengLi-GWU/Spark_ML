package com.project.conf
import javafx.application.Application
import javafx.event.ActionEvent
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.layout.StackPane
import javafx.scene.text.Text
import javafx.stage.Stage

import java.awt.Desktop
import java.lang.Thread.sleep
import java.net.{URI, URL}

object HelloWorld
{
  def main(args: Array[String])
  {
    Application.launch(classOf[HelloWorld], args: _*)
  }
}

class HelloWorld extends Application
{
  override def start(primaryStage: Stage)
  {
    val root = new StackPane
    val popup_window = new Stage
    popup_window.setTitle("This is a pop up window")
    primaryStage.setScene(new Scene(root, 500, 500))
    val ip = tracker.track_ip()
    val ip_address = new Text("Your IP Address is "+ip)
    primaryStage.setTitle("GUI")

    // master

    val master_bat = new Auto_Hive_Conf() // create Auto_Hive_Conf
    master_bat.write_master_nodes_bat() // auto write batch file
    val master = new Button // new button master
    val master_created = new Text("Master Created at "+ip+":8080")
    master.setText("create master")
    master.setOnAction((e: ActionEvent) => {
      Runtime.getRuntime.exec("start_master.bat")
      sleep(5000)
      if (Desktop.isDesktopSupported) {
        Desktop.getDesktop.browse(new URI("http://localhost:8080/")) //will bring you to master website
      }
      root.getChildren.remove(master)
      println("master created")
      root.getChildren.add(master_created)
    })

    // Worker

    val worker = new Button
    worker.setText("add worker")
    val worker_bat = new Auto_Hive_Conf()
    var count = 0
    worker_bat.write_worker_nodes_bat()
    worker.setOnAction((e: ActionEvent) => {
      Runtime.getRuntime.exec("start_worker.bat")
      root.getChildren.remove(worker)
      println("worker added")
    })

    // Submit
    val submit = new Button
    submit.setText("submit work")

    // location
    root.getChildren.add(ip_address) //ip_text
    ip_address.setTranslateX(0)
    ip_address.setTranslateY(-130)
    root.getChildren.add(master) //master
    master.setTranslateX(0)
    master.setTranslateY(-100)
    master_created.setTranslateX(0)
    master_created.setTranslateY(-100)
    root.getChildren.add(worker) //worker
    worker.setTranslateX(0)
    worker.setTranslateY(0)
    root.getChildren.add(submit) //submit
    submit.setTranslateX(0)
    submit.setTranslateY(100)

    primaryStage.show
  }

}

