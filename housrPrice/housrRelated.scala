import org.apache.log4j.Level
import org.apache.log4j.Logger
import org.apache.spark.sql.Dataset
import org.apache.spark.sql.Row
import org.apache.spark.sql.SparkSession

class housrRelated {

}

object housrRelated{
  def main(args: Array[String]): Unit = {
    System.out.println("Hello world!")

    System.setProperty("hadoop.home.dir", "D:/melo_project/environment/Hadoop")
    Logger.getLogger("org.apache").setLevel(Level.WARN)

    val spark: SparkSession = SparkSession.builder.appName("House Price Analysis").config("spark.sql.warehouse.dir", "file:///c:/tmp/").master("local[*]").getOrCreate

    var csvData: Dataset[Row] = spark.read.option("header", true).option("inferSchema", true).csv("src/resources/kc_house_data.csv")

    //        csvData.describe().show();
    csvData = csvData.drop("id", "date", "waterfront", "view", "condition", "grade", "yr_renovated", "zipcode", "lat", "long")


    for (col <- csvData.columns) {
      System.out.println("The correlation between the price and " + col + " is " + csvData.stat.corr("price", col))
    }

    csvData = csvData.drop("sqft_lot", "sqft_lot15", "yr_built", "sqft_living15")

    for (col1 <- csvData.columns) {
      for (col2 <- csvData.columns) {
        System.out.println("The correlation between " + col1 + " and " + col2 + " is " + csvData.stat.corr(col1, col2))
      }
    }
  }
}
