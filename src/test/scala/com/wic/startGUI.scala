package com.wic

import com.wic.gui.HelloWorld
import javafx.application.Application

object startGUI {

  def main(args: Array[String])
  {
    Application.launch(classOf[HelloWorld], args: _*)
  }

}
