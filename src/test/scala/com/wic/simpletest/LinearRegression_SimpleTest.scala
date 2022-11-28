package com.wic.simpletest

import com.wic.ml.{LinearRegression, Preprocess}
import org.apache.spark.sql.functions.col

object LinearRegression_SimpleTest {
  def main(args: Array[String]): Unit = {
    // use default master local[*]
    /*
    If you have a master node set master with your ip by
    var model: LinearRegression = new LinearRegression("spark://"your ip":7077")
    default running code in local
    parameters for K mean
    LinearRegression(master:String = "local[*]",
                appName:String = "KMeans",
                spark_message: Boolean = false)

     Use spark_message to show or not show message from spark
     */
    var model: LinearRegression = new LinearRegression()
    var csvData = model.read_csv("data/kc_house_data.csv")
    //calculate the percentage of sqft and create this colum
    csvData = csvData.withColumn("sqft_above_percentage", col("sqft_above").divide(col("sqft_living")))
    csvData = Preprocess.to_onehot(csvData,Array[String]("condition", "grade", "zipcode"))
    csvData = csvData.withColumnRenamed("price","label")
    csvData = Preprocess.to_Fearure_vector(csvData,Array[String]("condition", "grade", "zipcode"),true)
    var (train_set,test_set) = Preprocess.RandomTestSet(csvData,0.8)
    model.fit(train_set)
    model.evaluation(test_set)


    //
  }
}
