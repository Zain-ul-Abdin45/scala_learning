import scala.collection.immutable.List

class Main {
  def main(args: Array[String]): Unit = {
    val list = List(1, 2, 3, 4, 5)
    val doubleList = map(list, x => x * 2)
    println(doubleList)
  }
}
