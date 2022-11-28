package com.wic.simpletest

import com.wic.ml.{DecisionTree, Preprocess}
import org.apache.spark.sql.api.java.UDF1
import org.apache.spark.sql.functions.{call_udf, col, lit, when}
import org.apache.spark.sql.types.DataTypes

import java.util

object DecisionTree_Simpletest {
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
    // use default master local[*]
    /*
    If you have a master node set master with your ip by
    var model: KMean = new KMean("spark://"your ip":7077")
    default running code in local
    parameters for K mean
    KMean(master:String = "local[*]",
                appName:String = "KMeans",
                spark_message: Boolean = false)
    def fit(data: DataFrame,num_Clusters: Int)
    def tune(data: DataFrame, min_clusters:Int,max_clusters :Int)
     */
    var model: DecisionTree = new DecisionTree()
    var csvData = model.read_csv("data/vppFreeTrials.csv")
    model.spark.udf.register("countryGrouping", countryGrouping, DataTypes.StringType)
    csvData = csvData.withColumn("country", call_udf("countryGrouping", col("country")))
      .withColumn("label", when(col("payments_made").geq(1), lit(1)).otherwise(lit(0)))
    csvData = Preprocess.to_Index(csvData,"country")
    csvData = Preprocess.to_Fearure_vector(csvData,Array[String]("country", "rebill_period", "chapter_access_count", "seconds_watched"),true)
    var (train_input, test_input) = Preprocess.RandomTestSet(csvData,0.8)
    model.fit(train_input,3)
    model.evaluate(test_input)
    model.tune(train_input,test_input,1,10)
  }
}
