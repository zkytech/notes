package com.zkytech.spark.rdd.operator.transfer

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object Spark42_RDD_Operator24 {
  def main(args: Array[String]): Unit = {

    val sparkConf = new SparkConf().setMaster("local[*]").setAppName("File - RDD").set("spark.ui.port", "22112")
    val sc = new SparkContext(sparkConf)
//    val rdd: RDD[(String, Int)] = sc.makeRDD(List(
//      ("a", 1), ("b", 2), ("c", 3),
//      ("b", 4), ("c", 5), ("c", 6)), 2)
val rdd: RDD[(User, Int)] = sc.makeRDD(List((new User(), 1), (new User(), 2), (new User(), 3)))
    val sortRDD: RDD[(User, Int)] = rdd.sortByKey(true)
    println(sortRDD.collect().mkString(","))

    sc.stop()
  }
}
// 如果自定义key进行排序，需要将key混入特质Orderrd
class User extends Ordered[User] with Serializable {
  override def compare(that: User): Int = {
    1

  }
}
