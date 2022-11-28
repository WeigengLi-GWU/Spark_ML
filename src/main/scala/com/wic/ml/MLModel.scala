package com.wic.ml

import org.apache.log4j.{Level, Logger}
import org.apache.spark.ml.feature.VectorAssembler
import org.apache.spark.sql.{DataFrame, SparkSession}

class MLModel(master:String ,appName:String ,spark_message: Boolean ){


  if( spark_message == false){
    Logger.getLogger("org").setLevel(Level.OFF)
    Logger.getLogger("akka").setLevel(Level.OFF)
  }
  val spark = SparkSession.builder
    .appName(appName)
    .config("spark.sql.warehouse.dir", "file:///c:/tmp/")
    .master(master)
    .getOrCreate

  def read_csv(address: String, header: Boolean = true, inferSchema: Boolean = true): DataFrame = {
    var csvData = this.spark.read
      .option("header", header)
      .option("inferSchema", inferSchema)
      .csv(address)
    return csvData
  }

  def data_transform(csvData: DataFrame): DataFrame = {
    val vectorAssembler = new VectorAssembler
    val inputData = vectorAssembler.setInputCols(Array[String]("Gender", "Age", "Height", "Weight", "NoOfReps"))
      .setOutputCol("features")
      .transform(csvData)
      .select("features")
    return inputData
  }

}
