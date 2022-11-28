import org.apache.log4j.{Level, Logger}
import org.apache.spark.SparkConf
import org.apache.spark.ml.classification.MultilayerPerceptronClassifier
import org.apache.spark.ml.evaluation.MulticlassClassificationEvaluator
import org.apache.spark.sql.SparkSession

object MLP_MNIST {
  def main(args: Array[String]): Unit = {
    Logger.getLogger("org").setLevel(Level.OFF)
    Logger.getLogger("akka").setLevel(Level.OFF)
    val conf = new SparkConf().setMaster("spark://192.168.1.13:7077").setAppName("MLP_MNIST")
    val mySpark = SparkSession
      .builder()
      .appName("MLP_MNIST")
      .config(conf)
      .getOrCreate()


    // Load the data stored in LIBSVM format as a DataFrame.
    val data = mySpark.read.format("libsvm")
      .load("./data/mnist.txt")

    // Split the data into train and test
    val splits = data.randomSplit(Array(0.8, 0.2), seed = 1234L)
    val train = splits(0)
    val test = splits(1)

    // specify layers for the neural network:
    // input layer of size 780 (features), two intermediate of size 128 and 128
    // and output of size 10 (classes)
    val layers = Array[Int](780, 128, 128, 10)

    val t = System.nanoTime
    // create the trainer and set its parameters
    val trainer = new MultilayerPerceptronClassifier()
      .setLayers(layers)
      .setBlockSize(512)
      .setSeed(1234L)
      .setMaxIter(500)

    // train the model
    val model = trainer.fit(train)

    // compute accuracy on the test set
    val result = model.transform(test)
    val predictionAndLabels = result.select("prediction", "label")
    val evaluator = new MulticlassClassificationEvaluator()
      .setMetricName("accuracy")
    val time = (System.nanoTime - t) * 0.000000001 //System.nanoTime为纳秒，转化为秒
    println("运行时长：" + time + "s")
    println(s"Test set accuracy = ${evaluator.evaluate(predictionAndLabels)}")
  }
}
