
package com.wic.ml
import org.apache.log4j.{Level, Logger}
import org.apache.spark.ml.evaluation.RegressionEvaluator
import org.apache.spark.ml.feature.{OneHotEncoder, StringIndexer, VectorAssembler}
import org.apache.spark.ml.regression.{LinearRegression, LinearRegressionModel}
import org.apache.spark.ml.tuning.{ParamGridBuilder, TrainValidationSplit}
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._


object VPPChapterViewsLinear {
  def main(args: Array[String]): Unit = {
//    System.setProperty("hadoop.home.dir", "D:/CSCI6221/Hadoop")
    Logger.getLogger("org.apache").setLevel(Level.WARN)

    val spark = SparkSession.builder()
      .appName("VPP Chapter Views")
      .config("spark.sql.warehouse.dir", "file:///c:/tmp/")
      .master("local[*]")
      .getOrCreate()

    var csvData = spark.read
      .option("header", true)
      .option("inferSchema", true)
      .csv("data/vppChapterViews/*.csv")

    csvData = csvData.filter("is_cancelled = false").drop("observation_date", "is_cancelled")

    csvData = csvData.withColumn("firstSub", when(col("firstSub").isNull, 0).otherwise(col("firstSub")))
      .withColumn("all_time_views", when(col("all_time_views").isNull, 0).otherwise(col("all_time_views")))
      .withColumn("last_month_views", when(col("last_month_views").isNull, 0).otherwise(col("last_month_views")))
      .withColumn("next_month_views", when(col("next_month_views").isNull, 0).otherwise(col("next_month_views")))

    csvData = csvData.withColumnRenamed("next_month_views", "label")

    val payMethodIndexer = new StringIndexer
    csvData = payMethodIndexer.setInputCol("payment_method_type")
      .setOutputCol("payIndex")
      .fit(csvData)
      .transform(csvData)

    val countryIndexer = new StringIndexer
    csvData = countryIndexer.setInputCol("country")
      .setOutputCol("countryIndex")
      .fit(csvData)
      .transform(csvData)

    val periodIndexer = new StringIndexer
    csvData = periodIndexer.setInputCol("rebill_period_in_months")
      .setOutputCol("periodIndex")
      .fit(csvData)
      .transform(csvData)

    val encoder = new OneHotEncoder
    csvData = encoder.setInputCols(Array[String]("payIndex", "countryIndex", "periodIndex"))
      .setOutputCols(Array[String]("payVector", "countryVector", "periodVector"))
      .fit(csvData)
      .transform(csvData)

    val vectorAssembler = new VectorAssembler
    val inputData = vectorAssembler
      .setInputCols(Array[String]("firstSub", "age", "all_time_views", "last_month_views", "payVector", "countryVector", "periodVector"))
      .setOutputCol("features")
      .transform(csvData)
      .select("label", "features")

    val trainAndHoldoutData = inputData.randomSplit(Array[Double](0.9, 0.1))
    val trainAndTestData = trainAndHoldoutData(0)
    val holdOutData = trainAndHoldoutData(1)

    val lr = new LinearRegression

    val pgb = new ParamGridBuilder
    val paramMap = pgb.addGrid(lr.regParam, Array[Double](0.01, 0.1, 0.3, 0.5, 0.7, 1))
      .addGrid(lr.elasticNetParam, Array[Double](0, 0.5, 1))
      .build

    val tvs = new TrainValidationSplit
    tvs.setEstimator(lr)
      .setEvaluator(new RegressionEvaluator().setMetricName("r2"))
      .setEstimatorParamMaps(paramMap)
      .setTrainRatio(0.9)

    val model = tvs.fit(trainAndTestData)

    val lrModel = model.bestModel.asInstanceOf[LinearRegressionModel]

    System.out.println("The R2 value is " + lrModel.summary.r2)
    System.out.println("coefficients : " + lrModel.coefficients + " intercept : " + lrModel.intercept)
    System.out.println("reg param : " + lrModel.getRegParam + " elastic net param : " + lrModel.getElasticNetParam)
  }
}
