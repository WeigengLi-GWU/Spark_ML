package com.wic.ml

import org.apache.log4j.{Level, Logger}
import org.apache.spark.ml.classification.{DecisionTreeClassifier, RandomForestClassifier}
import org.apache.spark.ml.evaluation.MulticlassClassificationEvaluator
import org.apache.spark.ml.feature.{StringIndexer, VectorAssembler}
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.api.java.UDF1
import org.apache.spark.sql.functions._
import org.apache.spark.sql.types.DataTypes

import java.util

object decisionTree {

  var countryGrouping: UDF1[String, String] = new UDF1[String, String]() {
    @throws[Exception]
    override def call(country: String): String = {
      val topCountries = util.Arrays.asList(Array[String]("GB", "US", "IN", "UNKNOWN"))
      val europeanCountries = util.Arrays.asList(Array[String]("BE", "BG", "CZ", "DK", "DE", "EE", "IE", "EL", "ES", "FR", "HR", "IT", "CY", "LV", "LT", "LU", "HU", "MT", "NL", "AT", "PL", "PT", "RO", "SI", "SK", "FI", "SE", "CH", "IS", "NO", "LI", "EU"))
      if (topCountries.contains(country)) return country
      if (europeanCountries.contains(country)) "EUROPE"
      else "OTHER"
    }
  }

  def main(args: Array[String]): Unit = {
    //    System.setProperty("hadoop.home.dir", "D:/CSCI6221/Hadoop")
    Logger.getLogger("org.apache").setLevel(Level.WARN)

    val spark = SparkSession.builder
      .appName("VPP Chapter Views")
      .config("spark.sql.warehouse.dir", "file:///c:/tmp/")
      .master("local[*]")
      .getOrCreate

    spark.udf.register("countryGrouping", countryGrouping, DataTypes.StringType)

    var csvData = spark.read
      .option("header", true)
      .option("inferSchema", true)
      .csv("data/vppFreeTrials.csv")
    csvData.show()

    csvData = csvData.withColumn("country", callUDF("countryGrouping", col("country")))
      .withColumn("label", when(col("payments_made").geq(1), lit(1)).otherwise(lit(0)))

    csvData.show()

    val countryIndexer = new StringIndexer
    csvData = countryIndexer.setInputCol("country").setOutputCol("countryIndex").fit(csvData).transform(csvData)

    val vectorAssembler = new VectorAssembler
    vectorAssembler.setInputCols(Array[String]("countryIndex", "rebill_period", "chapter_access_count", "seconds_watched"))
    vectorAssembler.setOutputCol("features")
    val inputData = vectorAssembler.transform(csvData).select("label", "features")
    inputData.show()

    val trainingAndHoldoutData = inputData.randomSplit(Array[Double](0.8, 0.2))
    val trainingData = trainingAndHoldoutData(0)
    val holdoutData = trainingAndHoldoutData(1)

    val dtClassifier = new DecisionTreeClassifier
    dtClassifier.setMaxDepth(3)

    val model = dtClassifier.fit(trainingData)

    val predictions = model.transform(holdoutData)
    predictions.show()

    System.out.println(model.toDebugString)

    val evaluator = new MulticlassClassificationEvaluator
    evaluator.setMetricName("accuracy")
    System.out.println("The accuracy of the model is " + evaluator.evaluate(predictions))

    val rfClassifier = new RandomForestClassifier
    rfClassifier.setMaxDepth(3)
    val rfModel = rfClassifier.fit(trainingData)
    val predictions2 = rfModel.transform(holdoutData)
    predictions2.show()

    System.out.println(rfModel.toDebugString)
    System.out.println("The accuracy of the forest model is " + evaluator.evaluate(predictions2))

  }

}
