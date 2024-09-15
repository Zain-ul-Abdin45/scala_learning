import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession


val sparkContext = SparkSession.builder().appName("My application").master("local[*]").getOrCreate()
val data = spark.read.textFile("C:/Users/ZAIN UL ABDIN/Downloads/census_dataaccess-to-basic-amenities-total-responses-2018-census-csv");
