package com.wic.ml

import org.apache.spark.ml.clustering.{KMeans, KMeansModel}
import org.apache.spark.ml.evaluation.ClusteringEvaluator
import org.apache.spark.sql.DataFrame

class KMean(master:String = "local[*]",
            appName:String = "KMeans",
            spark_message: Boolean = false) extends MLModel(master,appName,spark_message) {

  var model : KMeansModel = _
  var predictions : DataFrame = _
  var num_cluster: Int = _
  var distance : Double = _


  def fit(inputData: DataFrame,num_Clusters: Int): Unit ={
    if (num_Clusters < 1) {
      println("Number of Cluster must bigger than 1")
      return
    }
    val kMeans = new KMeans
    kMeans.setK(num_Clusters)
    this.num_cluster = num_Clusters
    this.model = kMeans.fit(inputData)
    this.predictions = model.transform(inputData)
    //			Vector[] clusterCenters = model.clusterCenters();
    //			for (Vector v : clusterCenters) { System.out.println(v);}

    //      System.out.println("SSE is " + model.computeCost(inputData))
    val evaluator = new ClusteringEvaluator
    this.distance = evaluator.evaluate(predictions)
  }

  def tune(inputData: DataFrame, min_clusters:Int,max_clusters :Int): Unit ={
    if (min_clusters <=1){
      println("Number of Cluster must bigger than 1")
      return
    }else if(max_clusters<min_clusters){
      println("max_clusters smaller than min")
      return
    }
    val kMeans = new KMeans
    var max_distance : Double = 0
    var best_cluster :Int = 0
    var distant_list: List[Double] =  List[Double]()
    val evaluator = new ClusteringEvaluator

    for (num_Clusters <- min_clusters to max_clusters) {
      kMeans.setK(num_Clusters)
      val model = kMeans.fit(inputData)
      val predictions = model.transform(inputData)
      //      System.out.println("SSE is " + model.computeCost(inputData))
      var distance = evaluator.evaluate(predictions)
      if (distance>max_distance){
        max_distance = distance
        best_cluster = num_Clusters
      }
      distant_list = distance+:distant_list
    }
    println("Best model with number of cluster between "+min_clusters+" and "+max_clusters+" is")
    this.fit(inputData,best_cluster)
    this.info()
  }

  def info(): Unit = {
    println("Number of clusters: " + this.num_cluster)
    println("Cluster for training examples")
    this.predictions.show()
    println("Summary for each Cluster")
    this.predictions.groupBy("prediction").count.show()
    println("Slihouette with squared euclidean distance is " + this.distance)
  }

  def save(path:String): Unit ={
    this.model.save(path)
  }

}
