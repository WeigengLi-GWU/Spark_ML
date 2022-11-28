package com.wic.ml
import org.apache.spark.ml.feature.{OneHotEncoder, StringIndexer, VectorAssembler}
import org.apache.spark.sql.{DataFrame, SparkSession}
import com.wic.ml.KMean
object Preprocess {
  var kmean = new KMean()
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


}
