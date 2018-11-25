
import org.apache.spark.{SparkConf, SparkContext}
//import org.apache.spark.sql.SQLContext
import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession

case class test_profile(
                         id: Double,
                         name: String,
                         dob: String,
                         nationlaity: Double,
                         gender: Double,
                         weightage: Double
                       )
object FirstTry {
  val conf = new SparkConf()
    .setMaster("local[*]")
    .setAppName("sparkTest")
    .set("spark.cassandra.connection.host", "localhost")
    .set("spark.cassandra.connection.native.port", "9042")

  //val sc = new SparkContext(conf)

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



  df.write
    .format("org.apache.spark.sql.cassandra")
    .mode("overwrite")
    .option("confirm.truncate","true")
    .option("spark.cassandra.connection.host","localhost")
    .option("spark.cassandra.connection.port","9042")
    .option("keyspace","vision_app")
    .option("table","test_profile")
    .save()

}
