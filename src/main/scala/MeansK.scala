import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.mllib.clustering.{KMeans, KMeansModel}
import org.apache.spark.mllib.linalg.Vectors
import com.datastax.spark.connector._
import org.apache.spark
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.{DataFrame, Dataset, SparkSession}
import org.apache.spark.sql.functions._
import org.apache.spark.sql.types._
import org.apache.spark.ml.feature.Word2Vec
import org.apache.spark.ml.linalg.Vector
import org.apache.spark.sql.Row
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.SQLContext



object MeansK {

  def main(args: Array[String]) {

    val conf = new SparkConf()
      .setMaster("local[*]")
      .setAppName("sparkTest")
      .set("spark.cassandra.connection.host", "localhost")
    val sc = new SparkContext(conf)

    val spark = SparkSession
      .builder
      .appName("sparkTest")
      .config(conf)
      .master("local[2]")
      .getOrCreate()
    println("Spark Session Config Done")



//connecting cassandra and spark
    val df =spark.read
      .format("org.apache.spark.sql.cassandra")
      .option("spark.cassandra.connection.host","localhost")
      .option("spark.cassandra.connection.port","9042")
      .options(Map("table" -> "test_profile", "keyspace" -> "vision_app", "cluster" -> "Test Cluster"))


    val createDDL = """CREATE  table test_profile
     USING org.apache.spark.sql.cassandra
     OPTIONS (
     table "test_profile",
     keyspace "vision_app",
         pushdown "true")"""
    spark.sql(createDDL) // Creates Catalog Entry registering an existing Cassandra Table



    val df1 = spark.sql("select weightage,name from test_profile")

    final case class profile(weightage: Int, name: String)

    //val profiles = Seq(df1).zipWithIndex.map(_.swap).toDF("id", "name").as[profile]

    // Prepare data for k-means
    // Pass profiles through a "pipeline" of transformers

    import org.apache.spark.ml.feature._
    val tok = new RegexTokenizer()
      .setInputCol("name")
      .setOutputCol("tokens")
      .setPattern("\\W+")

    val hashTF = new HashingTF()
      .setInputCol("tokens")
      .setOutputCol("features")
      .setNumFeatures(20)

    val preprocess = (tok.transform _).andThen(hashTF.transform)

    val features = preprocess(df1.toDF)


    import org.apache.spark.ml.clustering.KMeans
    val kmeans = new KMeans

    val kmModel = kmeans.fit(features.toDF)

    kmModel.clusterCenters.map(_.toSparse)


    val result = kmModel.transform(preprocess(df1))



    val result1 = result.show()

  }
}
