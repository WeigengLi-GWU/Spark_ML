package com.wic.ml
import org.apache.log4j.Logger
import org.apache.log4j.Level
import org.apache.spark.SparkContext

object WordCount {
  def main(args: Array[String]): Unit = {
    Logger.getLogger("org.apache").setLevel(Level.WARN)

//    val sc = new SparkContext("spark://192.168.1.14:7077", "WordCount")
    val sc = new SparkContext("local[*]", "WordCount")

    val file = sc.textFile("./WordCount.txt")

    // 先分割成单词数组，然后合并，再与1形成KV映射
    val result = file.flatMap(value => value.split(" "))
      .map(value => (value, 1))
      .reduceByKey((a, b) => a + b)
      .sortBy(value => -value._2)

    result.foreach(value => println(value))
  }

}
