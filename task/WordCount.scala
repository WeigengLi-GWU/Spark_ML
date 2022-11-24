import org.apache.log4j.Logger
import org.apache.log4j.Level
import org.apache.spark.SparkContext
import org.apache.spark.ml.feature.VectorAssembler

/**
 * @author JavaEdge
 * @date 2019-04-09
 *
 */

object WordCount {
  def main(args: Array[String]): Unit = {
    Logger.getLogger("org.apache").setLevel(Level.WARN);

    //    val sc = new SparkContext("spark://192.168.1.14:7077", "WordCount")
    val sc = new SparkContext("local[*]", "WordCount")

    val file = sc.textFile("D:/test.txt")

    // 先分割成单词数组，然后合并，再与1形成KV映射
    val result = file.flatMap(_.split(" ")).map((_, 1)).reduceByKey((a, b) => a + b).sortBy(_._2)
    result.foreach(println(_))
  }

}
