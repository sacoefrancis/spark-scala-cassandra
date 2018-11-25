import org.apache.spark.sql.execution.streaming.FileStreamSource.Timestamp
import org.apache.spark.{SparkConf, SparkContext}
//import org.apache.spark.sql.SQLContext
import org.apache.spark.SparkConf


val conf = new SparkConf()
  .setMaster("local[*]")
  .setAppName("sparkTest")
val sc = new SparkContext(conf)
import org.apache.spark.sql.SQLContext
import org.apache.spark.{SparkConf, SparkContext}


import org.apache.spark.sql.SparkSession



val data = sc.textFile("/home/test_profile.csv")

data.saveAsObjectFile("/home/t.txt")


//}