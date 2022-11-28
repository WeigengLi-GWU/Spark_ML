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
    DecisionTree(master:String = "local[*]",
                appName:String = "KMeans",
                spark_message: Boolean = false)
     */
    var model: KMean = new KMean()
    var csvData = model.read_csv("data/GymCompetition.csv")
    csvData = Preprocess.to_onehot(csvData, "Gender")
    var inputdata = Preprocess.to_Fearure_vector(csvData,Array[String]("Gender", "Age", "Height", "Weight", "NoOfReps"))
    model.fit(inputdata, 3)
    model.info()
    model.tune(inputdata,2,8)
    model.info()
  }
}
