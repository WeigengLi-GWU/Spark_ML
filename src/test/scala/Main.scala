import org.apache.log4j.{Level, Logger}
import org.apache.spark.ml.recommendation.{ALS, ALSModel}
import org.apache.spark.sql.functions.col
import org.apache.spark.sql.{Dataset, Row, SparkSession}

import java.util.List

object Main {
  def main(args: Array[String]): Unit = {
    Logger.getLogger("org.apache").setLevel(Level.WARN)

    val spark: SparkSession = SparkSession.builder
      .appName("VPP Chapter Views")
      .config("spark.sql.warehouse.dir", "file:///c:/tmp/")
      .master("local[*]")
      .getOrCreate

    var csvData: Dataset[Row] = spark.read.option("header", true).option("inferSchema", true).csv("src/test/scala/VPPcourseViews.csv")

    csvData.show()
    //make it becomes integer
    csvData = csvData.withColumn("proportionWatched", col("proportionWatched").multiply(100))

    //create a table to watch the data
    //        csvData.groupBy("userId").pivot("courseId").sum("proportionWatched").show();

    val als: ALS = new ALS()
      .setMaxIter(10)
      .setRegParam(0.1)
      .setUserCol("userId")
      .setItemCol("courseId")
      .setRatingCol("proportionWatched")

    val model: ALSModel = als.fit(csvData)

    val userRecs: Dataset[Row] = model.recommendForAllUsers(5)
    //        userRecs.show();
    val userRecsList: List[Row] = userRecs.takeAsList(5)

    for (r <- 0 to userRecsList.size() - 1) {
      val userId = userRecsList.get(r).get(0)
      val recs = userRecsList.get(r).get(1)
      println("User " + userId + " might want to recommend" + recs)
      println("This user has already watched: ")
      csvData.filter("userId =" + userId).show()
    }

    //        csvData.show();
  }
}