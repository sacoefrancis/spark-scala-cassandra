//stop old context
//sc.stop

//import library for configuration
import org.apache.spark.sql.execution.streaming.FileStreamSource.Timestamp
import org.apache.spark.{SparkConf, SparkContext}
//import org.apache.spark.sql.SQLContext
import org.apache.spark.SparkConf

val conf = new SparkConf()
  .setMaster("local[*]")
  .setAppName("sparkTest")
val sc = new SparkContext(conf)

//create new configuration
//val conf = (new SparkConf(true)
//			.set("spark.cassandra.connection.host", "localhost"))

//import context library
//import org.apache.spark.SparkContext

//create new context
//val sc = new SparkContext(conf)

//create class train


//load csv data
val data = sc.textFile("/root/project/test_profile.csv")

//parse data to class
val parsed = data.filter(line => !line.contains("id"))
  .map {row =>
    val splitted = row.split(",")
    val Array(id,name,dob,nationlaity,gender,weightage) = splitted.slice(0,6).map(row=> (row(id).toDouble,row(name).toString,row(dob).toString,row(nationlaity).toDouble,row(gender).toDouble,row(weightage).toDouble))
    test_profile (id._1,name._2,dob._3,nationlaity._4,gender._5,weightage._6)
  }




//check parsing results
parsed.take(2).foreach(println)
parsed.count

//import Cassandra connectors
import com.datastax.spark.connector._
import com.datastax.spark.connector.cql.CassandraConnector

//save data to Cassandra
parsed.saveToCassandra ("vision_app", "test_profile")

case class test_profile(
                         id: Double,
                         name: String,
                         dob: String,
                         nationlaity: Double,
                         gender: Double,
                         weightage: Double
                       )

//load trainHistory from cassandra
val rdd = sc.cassandraTable[test_profile]("vision_app", "test_profile").cache()

//check loaded data
rdd.count
rdd.first

//import RDD and Vectors libraries
import org.apache.spark.rdd.RDD
import org.apache.spark.mllib.linalg.Vector
import org.apache.spark.mllib.linalg.Vectors

//create vector of repeattrips observations
val observations1: RDD[Vector] =
  rdd.map(s => Vectors.dense
  (s.weightage))


//import statistics library
import org.apache.spark.mllib.stat.{MultivariateStatisticalSummary, Statistics}


//calculate statistics for repeattrips
val summary: MultivariateStatisticalSummary = Statistics.colStats(observations1)

//print out statistics
println(summary.count)
println(summary.max)
println(summary.mean)
println(summary.min)
println(summary.variance)



//print out
scala.tools.nsc.io.File("output.txt").writeAll("\n" + "weightage.count = "
  + summary.count + "\n")

scala.tools.nsc.io.File("output.txt").appendAll("\n" + "weightage.max = "
  + summary.max + "\n")

scala.tools.nsc.io.File("output.txt").appendAll("\n" + "weightage.mean = "
  + summary.mean + "\n")

scala.tools.nsc.io.File("output.txt").appendAll("\n" + "weightage.min = "
  + summary.min + "\n")

scala.tools.nsc.io.File("output.txt").appendAll("\n" + "weightage.variance = "
  + summary.variance + "\n")




//import library
import org.apache.spark.mllib.clustering.KMeans

//prepare data
val trainingData: RDD[Vector] =
  rdd.map(s => Vectors.dense
  (s.id,s.weightage))

//set params
val numClusters = 2
val numIterations = 10

//clustering
val clusters = KMeans.train(trainingData, numClusters, numIterations)

//cluster centers
clusters.clusterCenters

//save to output file
val save = sc.parallelize(clusters.clusterCenters)
save.saveAsTextFile("clusters")

//exit
//exit


