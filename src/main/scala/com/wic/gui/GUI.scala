package com.wic.gui
import javafx.application.Application
import javafx.event.ActionEvent
import javafx.scene.Scene
import javafx.scene.control.{Button, TextField}
import javafx.scene.layout.StackPane
import javafx.scene.text.{Font, Text}
import javafx.stage.Stage

import java.awt.Desktop
import java.lang.Thread.sleep
import java.net.URI

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
      sleep(1000)
      root.getChildren.remove(master)
      sleep(1000)
      root.getChildren.add(master_created)
      sleep(3000)
      if (Desktop.isDesktopSupported) {
        Desktop.getDesktop.browse(new URI("http://localhost:8080/")) //will bring you to master website
      }
    })

    // Worker
    val input_ip = new Text("Please input the Master IP You would like to join.")
    val worker = new Button
    worker.setText("add worker")
    val worker_bat = new Auto_Hive_Conf()
    val master_ip = new TextField(ip)
    master_ip.setPrefWidth(0.01)
    val textbox = new Text
    worker.setOnAction((e: ActionEvent) => {
      root.getChildren.remove(master_ip)
      root.getChildren.add(textbox)
      textbox.setText("you are now one of worker of "+master_ip.getText)
      worker_bat.write_worker_nodes_bat(master_ip.getText())
      Runtime.getRuntime.exec("start_worker.bat")
      root.getChildren.remove(worker)
    })

    // Submit
    //val submit = new Button
    //submit.setText("submit work")

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
    worker.setTranslateY(100)
    root.getChildren.add(input_ip)
    input_ip.setTranslateX(0)
    input_ip.setTranslateY(0)
    root.getChildren.add(master_ip) //master ip location
    master_ip.setTranslateX(0)
    master_ip.setTranslateY(50)
    textbox.setTranslateX(0)
    textbox.setTranslateY(50)
    //root.getChildren.add(submit) //submit
    //submit.setTranslateX(0)
    //submit.setTranslateY(150)

    primaryStage.show
  }

}

