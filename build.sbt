name := "sparkTest"

version := "0.1"

scalaVersion := "2.11.8"

// https://mvnrepository.com/artifact/org.apache.spark/spark-core
//libraryDependencies += "org.apache.spark" %% "spark-core" % "2.2.0"

libraryDependencies +=  "org.apache.spark" %% "spark-core" % "2.2.0" % "provided"
libraryDependencies +=   "org.apache.spark" %% "spark-streaming" % "2.2.0" % "provided"
libraryDependencies +=   "org.apache.spark" %% "spark-sql" % "2.2.0"
libraryDependencies += "com.datastax.spark" %% "spark-cassandra-connector" % "2.0.2"
//libraryDependencies +=  "com.datastax.spark.connector.types.TypeConverter.LocalDateConverter"
//libraryDependencies +=   "org.apache.spark" %% "spark-streaming_2.11" % "2.2.0"
//libraryDependencies +=   "org.apache.spark" %% "spark-sql_2.11" % "2.0.0" % "provided"
//libraryDependencies +=   "com.datastax.spark" %% "spark-cassandra-connector" % "2.0.1-s_2.11"
//libraryDependencies +=   "com.datastax.spark" %% "spark-cassandra-connector" % "1.0.0-beta1"

//libraryDependencies +=   "org.apache.spark" %% "spark-mllib" % "2.0.1"
libraryDependencies +=   "com.databricks" % "spark-csv_2.11" % "1.5.0"
//libraryDependencies +=     "com.datastax.cassandra" %% "cassandra-driver-core" % "3.0.0"
libraryDependencies +=   "org.apache.spark"  %%  "spark-mllib"   % "2.2.0"
//libraryDependencies += "datastax" %% "spark-cassandra-connector" % "2.3.2-s_2.11"
//libraryDependencies += "com.databricks" %% "spark-csv_2.10" % "1.4.0"
//libraryDependencies +=   "com.github.mpeltonen" %% "sbt-idea" % "0.10.0"






