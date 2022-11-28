package com.wic.simpletest

import org.apache.log4j.{Level, Logger}
import org.apache.spark.SparkContext

object WordCount {
  def main(args: Array[String]): Unit = {
    Logger.getLogger("org.apache").setLevel(Level.WARN)

    // set master
    val sc = new SparkContext("local[*]", "WordCount")

    val file = sc.textFile("data/WordCount.txt")

    // Split into word arrays, then merge. Next, build KV map with 1.
    val result = file.flatMap(value => value.split(" "))
      .map(value => (value, 1))
      .reduceByKey((a, b) => a + b)
      .sortBy(value => -value._2)

    result.foreach(value => println(value))
  }

}
