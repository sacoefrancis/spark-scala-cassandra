import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.execution.streaming.FileStreamSource.Timestamp
import org.apache.spark.{SparkConf, SparkContext}
import org.joda.time.DateTime
//import org.apache.spark.sql.SQLContext
import org.apache.spark.SparkConf


import org.apache.spark._
import org.apache.spark.SparkContext._
import com.datastax.driver.core.LocalDate


case class test_profile(
                         id: Double,
                         name: String,
                       //  dob: java.time.LocalDate,
                         nationlaity: Double,
                         weightage: Double,
                         gender: Double
                       )

object SaveCassandra {

 def main(args: Array[String]) {

    val conf = new SparkConf()
      .setMaster("local[*]")
      .setAppName("sparkTest")
      .set("spark.cassandra.connection.host", "localhost")
    //  .set("spark.cassandra.connection.native.port", "9042")

    val sc = new SparkContext(conf)

    val spark = SparkSession
      .builder
      .appName("sparkTest")
      .config(conf)
      .master("local[2]")
      .getOrCreate()
    println("Spark Session Config Done")



    val df =spark.read
      .format("csv")
      .option("header","true")
      .option("inferSchema","true")
      .option("nullValue","NA")
      .option("timestampFormat","yyyy-MM-dd'T'HH:mm:ss")
      .option("mode","failfast")
      .load("/home/francis/Desktop/test_profile.csv")



   println("Hello, world!")

   df.write
     .format("org.apache.spark.sql.cassandra")
     .option("spark.cassandra.connection.host","localhost")
     .option("spark.cassandra.connection.port","9042")
     .options(Map("table" -> "test_profile", "keyspace" -> "vision_app", "cluster" -> "Test Cluster"))
     .save()



    }
  }
//}

