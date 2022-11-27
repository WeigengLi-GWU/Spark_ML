package com.wic.ml
import org.apache.log4j.Level
import org.apache.log4j.Logger
import org.apache.spark.ml.clustering.KMeans
import org.apache.spark.ml.clustering.KMeansModel
import org.apache.spark.ml.evaluation.ClusteringEvaluator
import org.apache.spark.ml.feature.OneHotEncoder
import org.apache.spark.ml.feature.StringIndexer
import org.apache.spark.ml.feature.VectorAssembler
import org.apache.spark.ml.linalg.Vector
import org.apache.spark.sql.Dataset
import org.apache.spark.sql.Row
import org.apache.spark.sql.SparkSession


object GymCompetitorsClustering {
  def main(args: Array[String]): Unit = {
    Logger.getLogger("org.apache").setLevel(Level.WARN)

    val spark = SparkSession.builder
      .appName("Gym Competitors")
      .config("spark.sql.warehouse.dir", "file:///c:/tmp/")
      .master("local[*]")
      .getOrCreate

    var csvData = spark.read
      .option("header", true)
      .option("inferSchema", true)
      .csv("./GymCluster/GymCompetition.csv")

    //csvData.printSchema();

    val genderIndexer = new StringIndexer
    genderIndexer.setInputCol("Gender")
    genderIndexer.setOutputCol("GenderIndex")
    csvData = genderIndexer.fit(csvData).transform(csvData)

    val genderEncoder = new OneHotEncoder
    genderEncoder.setInputCols(Array[String]("GenderIndex"))
    genderEncoder.setOutputCols(Array[String]("GenderVector"))
    csvData = genderEncoder.fit(csvData).transform(csvData)

    csvData.show()

    val vectorAssembler = new VectorAssembler
    val inputData = vectorAssembler.setInputCols(Array[String]("GenderVector", "Age", "Height", "Weight", "NoOfReps"))
      .setOutputCol("features")
      .transform(csvData)
      .select("features")
    inputData.show()

    val kMeans = new KMeans
    for (noOfClusters <- 2 to 8) {
      kMeans.setK(noOfClusters)
      System.out.println("No of clusters " + noOfClusters)

      val model = kMeans.fit(inputData)
      val predictions = model.transform(inputData)
      predictions.show()

      //			Vector[] clusterCenters = model.clusterCenters();
      //			for (Vector v : clusterCenters) { System.out.println(v);}

      predictions.groupBy("prediction").count.show()

//      System.out.println("SSE is " + model.computeCost(inputData))

      val evaluator = new ClusteringEvaluator
      System.out.println("Slihouette with squared euclidean distance is " + evaluator.evaluate(predictions))
    }
  }
}
