import scala.io.Source
//import org.apache.spark.sql.SparkSession

case class Sale(product:String, region:String, amount:Double)
object SalesAnalyzer {
  def main(args:Array[String]): Unit={
    val salesData = loadSalesData("C:/Users/ZAIN UL ABDIN/Documents/scala_learnings/CollectionScala/Data/sales.csv")
    val totalSales = salesData.map(_.amount).sum
    val avgSalesPerRegion = salesData.groupBy(_.region).map {
      case (region, sales) => (region, sales.map(_.amount).sum / sales.size)
    }
    val topSellingProducts = salesData.groupBy(_.product).map {
      case (product, sales) => (product, sales.map(_.amount).sum)
    }.toSeq.sortBy(-_._2).take(5)
    println(s"Total Sales: $totalSales")
    println("Average Sales per Region:")
    avgSalesPerRegion.foreach { case (region, avg) => println(s"$region: $avg")}
    println("Top selling Products:")
    topSellingProducts.foreach {case (product, total) => println(s"$product: $total")}
  }
  def loadSalesData(filePath: String): Seq[Sale] = {
    Source.fromFile(filePath).getLines().drop(1).map {
      line => val Array(product, region, amount) = line.split(",")
        Sale(product, region, amount.toDouble)
    }.toSeq
  }
}