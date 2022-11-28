package com.wic.ml
import org.apache.spark.ml.classification.{DecisionTreeClassificationModel, DecisionTreeClassifier, RandomForestClassifier}
import org.apache.spark.ml.evaluation.MulticlassClassificationEvaluator
import org.apache.spark.ml.feature.{StringIndexer, VectorAssembler}
import org.apache.spark.sql.{DataFrame, SparkSession}
import org.apache.spark.sql.api.java.UDF1
import org.apache.spark.sql.functions._
import org.apache.spark.sql.types.DataTypes

class DecisionTree(master:String = "local[*]",
                   appName:String = "KMeans",
                   spark_message: Boolean = false) extends MLModel(master,appName,spark_message){

  var model: DecisionTreeClassifier = new DecisionTreeClassifier
  var MaxDepth: Int = _
  var distance: Double = _
  var predictions:DataFrame=_
  var traning_accurcy: Double =_
  var fitted_model:  DecisionTreeClassificationModel=_
  def fit(inputData:DataFrame,maxDepth:Int): Unit ={
    this.MaxDepth=maxDepth
    this.model = new DecisionTreeClassifier
    this.model .setMaxDepth(maxDepth)
    this.fitted_model = this.model.fit(inputData)
    this.predictions = this.fitted_model.transform(inputData)
    val evaluator = new MulticlassClassificationEvaluator
    evaluator.setMetricName("accuracy")
    this.traning_accurcy = evaluator.evaluate(predictions)
    System.out.println("The accuracy in training set model is " + traning_accurcy)
  }
  def info(): Unit ={
    println("Max depth"+MaxDepth)
    println()
    println(fitted_model.toDebugString)
  }
  def evaluate(dataset:DataFrame): Double ={
    val test_predictions = fitted_model.transform(dataset)
    val evaluator = new MulticlassClassificationEvaluator
    evaluator.setMetricName("accuracy")
    var test_acc = evaluator.evaluate(test_predictions)
    System.out.println("The accuracy of the model on test set is " + evaluator.evaluate(test_predictions))
    return test_acc
  }

  def save(path: String): Unit = {
    this.fitted_model.save(path)
  }

  def tune(tran_set: DataFrame,test_set:DataFrame,min_MaxDepth:Int,max_MaxDepth:Int): Unit ={
    if (min_MaxDepth<1){
      println("min_MaxDepth must greater than 0")
      return
    }else if(min_MaxDepth>max_MaxDepth){
      println("min_MaxDepth must smaller than max_MaxDepth")
      return
    }
    var max_accuracy =0.0
    var best_depth = 0
    for ( i <- min_MaxDepth to max_MaxDepth){
      this.fit(tran_set,i)
      println("evaluating model with max depth = "+i)
      var test_acc = this.evaluate(test_set)
      if (test_acc>max_accuracy){
        best_depth = i
        max_accuracy =test_acc
      }
    }
    println("model with best max_depth is "+best_depth)
    this.fit(tran_set,best_depth)
    this.evaluate(test_set)
  }

}
