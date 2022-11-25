package com.project.conf
import javafx.application.Application
import javafx.event.ActionEvent
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.layout.StackPane
import javafx.stage.Stage
import java.awt.Desktop
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
    primaryStage.setScene(new Scene(root, 500, 500))


    primaryStage.setTitle("GUI")
    val master_bat = new Auto_Hive_Conf() // create Auto_Hive_Conf
    master_bat.write_master_nodes_bat() // auto write batch file
    val master_url = tracker.track_ip()+":8080" // create the master URL
    val master = new Button // new button master
    master.setText("create master")
    master.setOnAction((e: ActionEvent) => {
      Runtime.getRuntime.exec("start_master.bat")
      if (Desktop.isDesktopSupported) {
        Desktop.getDesktop.browse(new URI("www.4399.com")) //will bring you to master website
      }
      println("master created")
    })
    val worker = new Button
    worker.setText("add worker")
    worker.setOnAction((e: ActionEvent) => {
      Runtime.getRuntime.exec("start_worker.bat")
      println("worker added")
    })

    root.getChildren.add(master)
    master.setTranslateX(-100)
    master.setTranslateY(0)
    root.getChildren.add(worker)
    worker.setTranslateX(100)
    worker.setTranslateY(0)
    primaryStage.show
  }

}
