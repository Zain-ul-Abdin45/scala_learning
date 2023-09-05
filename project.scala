package zain.learning.spark.examples //to use in other dependencies
import org.apache.spark.sql.{Dataset, Row, Session}
import org.apache.spark.sql.SparkSession
import org.apache.log4j.Logger

case class SurvayRecord(Age:Int, Gender:String, Country:String, state:String)

object HelloDataset extends Serializable {
    @transient lazy val logger: Logger = Logger.getLogger(getClass.getName)

    def main(args: Array[String]): Unit = {
        if (args.length == 0) {
            logger.error("Usage: HelloDataset filename")
            System.exit(1)
        }
    //application_name Reading Dataset
    //local[3] local mode with 3 CPUs
        val spark = SparkSession.builder().appName("Reading Dataset").master("local[3]").getOrCreate()

        import spark.implicits._


        val rawDF:Dataset[Row] = spark.read.option("header", "true").option("inferschema", "true").csv("sample.csv")
        val surveyDS:Dataset[SurvayRecord] = rawDF.select("Age", "Gender", "Country","State").as[SurvayRecord]

        // val filteredDS = SurvayDS.filter(r=>r.Age<40)
        val filteredDS = SurvayDS.filer("Age < 40")
        val countDS = filteredDS.groupByKey(r => r.Country).count()
        val countDF = filteredDF.groupBy("Country").count()

        logger.info("DataFrame: "+ countDF.collect().mkString(","))
        logger.info("Dataset: "+ countDS.collect().mkString(","))
        scala.io.StdIn.readLine()
        spark.stop()
    }
}