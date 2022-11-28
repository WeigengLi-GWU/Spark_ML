package com.wic.ml

import org.apache.spark.ml.evaluation.RegressionEvaluator
import org.apache.spark.ml.param.ParamMap
import org.apache.spark.ml.tuning.{ParamGridBuilder, TrainValidationSplit, TrainValidationSplitModel}
import org.apache.spark.ml.regression.{LinearRegression, LinearRegressionModel}
import org.apache.spark.sql.{DataFrame, Dataset}

class LinearRegression (master:String = "local[*]",
                        appName:String = "KMeans",
                        spark_message: Boolean = false) extends MLModel(master,appName,spark_message){

  var model:LinearRegressionModel =_

  def fit(train_set:DataFrame): Unit ={
      val linearRegression: org.apache.spark.ml.regression.LinearRegression = new org.apache.spark.ml.regression.LinearRegression
      val paramGridBuilder: ParamGridBuilder = new ParamGridBuilder

      val paramMap: Array[ParamMap] = paramGridBuilder
        .addGrid(linearRegression.regParam, Array[Double](0.01, 0.1, 0.5))
        .addGrid(linearRegression.elasticNetParam, Array[Double](0, 0.5, 1))
        .build

      val trainValidationSplit: TrainValidationSplit = new TrainValidationSplit()
        .setEstimator(linearRegression)
        .setEvaluator(new RegressionEvaluator().setMetricName("r2"))
        .setEstimatorParamMaps(paramMap)
        .setTrainRatio(0.8)

      val lrmodel: TrainValidationSplitModel = trainValidationSplit.fit(train_set)
      this.model = lrmodel.bestModel.asInstanceOf[LinearRegressionModel]
      System.out.println("The training data r2 value is " + model.summary.r2 + " and the RMSE is " + model.summary.rootMeanSquaredError)

      //model.transform(testData).show();

      System.out.println("coefficients : " + model.coefficients + " intercept : " + model.intercept)
      System.out.println("reg param : " + model.getRegParam + " elastic net param : " + model.getElasticNetParam)
    }

  def evaluation(test_set:DataFrame): Unit ={
    System.out.println("The test data r2 value is " + model.evaluate(test_set).r2 + " and the RMSE is " + model.evaluate(test_set).rootMeanSquaredError)
  }

  def info(): Unit = {
    System.out.println("The training data r2 value is " + model.summary.r2 + " and the RMSE is " + model.summary.rootMeanSquaredError)
    //model.transform(testData).show();
    System.out.println("coefficients : " + model.coefficients + " intercept : " + model.intercept)
    System.out.println("reg param : " + model.getRegParam + " elastic net param : " + model.getElasticNetParam)
  }

  def save(path: String): Unit = {
    this.model.save(path)
  }

}
