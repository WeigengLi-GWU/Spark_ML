package com.wic.ml
import org.apache.spark.ml.feature.{OneHotEncoder, StringIndexer, VectorAssembler}
import org.apache.spark.sql.{DataFrame, SparkSession}
import com.wic.ml.KMean
object Preprocess {
  def to_Index(dataframe: DataFrame, col_name: String): DataFrame ={
    val strIndexer = new StringIndexer
    var newdataframe = strIndexer.setInputCol(col_name).setOutputCol(col_name+"Index").fit(dataframe).transform(dataframe)
    newdataframe = newdataframe.drop(col_name)
    newdataframe = newdataframe.withColumnRenamed(col_name+"Index",col_name)
    return newdataframe
  }
  def to_onehot(dataframe: DataFrame, col_name: String): DataFrame = {
    val strIndexer = new StringIndexer
    strIndexer.setInputCol(col_name)
    strIndexer.setOutputCol(col_name + "Index")
    var newdata = strIndexer.fit(dataframe).transform(dataframe)
    newdata = newdata.drop(col_name)
    val ohEncoder = new OneHotEncoder
    ohEncoder.setInputCols(Array[String](col_name + "Index"))
    ohEncoder.setOutputCols(Array[String](col_name))
    newdata = ohEncoder.fit(newdata).transform(newdata)
    newdata = newdata.drop(col_name + "Index")
    return newdata
  }
  def to_onehot(data: DataFrame, col_name: Array[String]): DataFrame = {
    var newdata = to_onehot(data,(col_name(0)).toString())
    for (i <- 1 to col_name.length-1){
      newdata = to_onehot(newdata,col_name(i))
    }
    return newdata
  }


  def to_Fearure_vector(csvData: DataFrame,input_col:Array[String],with_lable:Boolean=false): DataFrame = {
    val vectorAssembler = new VectorAssembler
    // TODO: 注意输入的lable不要装换
    vectorAssembler.setInputCols(input_col)
      .setOutputCol("features")

    var inputData :DataFrame = null
    if (with_lable){
      inputData=vectorAssembler.transform(csvData).select("label","features")
    }else {
      inputData=vectorAssembler.transform(csvData).select("features")
    }
      return inputData

  }

  def RandomTestSet(inputData:DataFrame,train_rate: Double): (DataFrame,DataFrame) ={
    if (train_rate>1||train_rate<0){
      println("Rate of Training Set must between (0,1)")
    }
    val trainingAndHoldoutData = inputData.randomSplit(Array[Double](train_rate, 1-train_rate))
    val trainingData = trainingAndHoldoutData(0)
    val holdoutData = trainingAndHoldoutData(1)
    return (trainingData,holdoutData)
  }



}
