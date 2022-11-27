import org.apache.log4j.{Level, Logger}
import org.apache.spark.ml.evaluation.RegressionEvaluator
import org.apache.spark.ml.feature.{OneHotEncoder, StringIndexer, VectorAssembler}
import org.apache.spark.ml.param.ParamMap
import org.apache.spark.ml.regression.{LinearRegression, LinearRegressionModel}
import org.apache.spark.ml.tuning.{ParamGridBuilder, TrainValidationSplit, TrainValidationSplitModel}
import org.apache.spark.sql.{Dataset, Row, SparkSession}
import org.apache.spark.sql.functions.col

object housePricePredict {
  def main(args: Array[String]): Unit = {
    println("Hello world!")

//    System.setProperty("hadoop.home.dir", "D:/melo_project/environment/Hadoop")

    // only show the WARNNING log
    Logger.getLogger("org.apache").setLevel(Level.WARN)
    //build the spark and set the config of it
    val spark = SparkSession.builder()
      .master("local")
      .appName("House Price Analysis")
      .config("spark.sql.warehouse.dir", "file:///c:/tmp/")
      .getOrCreate()

//    print(spark)
    //read the file which need to clean, transform and analysis
    var csvData = spark.read.option("header", true).option("inferSchema", true).csv("./housePrice/kc_house_data.csv")

    csvData.show()

    csvData = csvData.withColumn("sqft_above_percentage", col("sqft_above").divide(col("sqft_living")))


      csvData.show()

//
//      val conditionIndexer: StringIndexer = new StringIndexer
//    conditionIndexer.setInputCol("condition")
//    conditionIndexer.setOutputCol("conditionIndex")
//    csvData = conditionIndexer.fit(csvData).transform(csvData)
//
//    val gradeIndexer: StringIndexer = new StringIndexer
//    gradeIndexer.setInputCol("grade")
//    gradeIndexer.setOutputCol("gradeIndex")
//    csvData = gradeIndexer.fit(csvData).transform(csvData)
//
//    val zipcodeIndexer: StringIndexer = new StringIndexer
//    zipcodeIndexer.setInputCol("zipcode")
//    zipcodeIndexer.setOutputCol("zipcodeIndex")
//    csvData = zipcodeIndexer.fit(csvData).transform(csvData)
//
//    val encoder: OneHotEncoder = new OneHotEncoder
//    encoder.setInputCols(Array[String]("conditionIndex", "gradeIndex", "zipcodeIndex"))
//    encoder.setOutputCols(Array[String]("conditionVector", "gradeVector", "zipcodeVector"))
//    csvData = encoder.fit(csvData).transform(csvData)
//
//    val vectorAssembler: VectorAssembler = new VectorAssembler().setInputCols(Array[String]("bedrooms", "bathrooms", "sqft_living", "sqft_above_percentage", "floors", "conditionVector", "gradeVector", "zipcodeVector", "waterfront")).setOutputCol("features")
//
//    val modelInputData: Dataset[Row] = vectorAssembler.transform(csvData).select("price", "features").withColumnRenamed("price", "label")
//
//    //modelInputData.show();
//
//    val dataSplits: Array[Dataset[Row]] = modelInputData.randomSplit(Array[Double](0.8, 0.2))
//    val trainingAndTestData: Dataset[Row] = dataSplits(0)
//    val holdOutData: Dataset[Row] = dataSplits(1)
//
//    val linearRegression: LinearRegression = new LinearRegression
//
//    val paramGridBuilder: ParamGridBuilder = new ParamGridBuilder
//
//    val paramMap: Array[ParamMap] = paramGridBuilder.addGrid(linearRegression.regParam, Array[Double](0.01, 0.1, 0.5)).addGrid(linearRegression.elasticNetParam, Array[Double](0, 0.5, 1)).build
//
//    val trainValidationSplit: TrainValidationSplit = new TrainValidationSplit().setEstimator(linearRegression).setEvaluator(new RegressionEvaluator().setMetricName("r2")).setEstimatorParamMaps(paramMap).setTrainRatio(0.8)
//
//    val model: TrainValidationSplitModel = trainValidationSplit.fit(trainingAndTestData)
//    val lrModel: LinearRegressionModel = model.bestModel.asInstanceOf[LinearRegressionModel]
//
//
//    System.out.println("The training data r2 value is " + lrModel.summary.r2 + " and the RMSE is " + lrModel.summary.rootMeanSquaredError)
//
//    //model.transform(testData).show();
//
//    System.out.println("The test data r2 value is " + lrModel.evaluate(holdOutData).r2 + " and the RMSE is " + lrModel.evaluate(holdOutData).rootMeanSquaredError)
//
//    System.out.println("coefficients : " + lrModel.coefficients + " intercept : " + lrModel.intercept)
//    System.out.println("reg param : " + lrModel.getRegParam + " elastic net param : " + lrModel.getElasticNetParam)


  }

}
