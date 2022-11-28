package com.wic.simpletest

import com.wic.ml.{KMean, Preprocess}
object KMean_Simpletest {

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
    var model: KMean = new KMean()
    var csvData = model.read_csv("data/GymCompetition.csv")
    csvData = Preprocess.to_onehot(csvData, "Gender")
    model.fit(csvData, 3)
    model.info()
    model.tune(csvData,2,8)
    model.info()
  }
}
