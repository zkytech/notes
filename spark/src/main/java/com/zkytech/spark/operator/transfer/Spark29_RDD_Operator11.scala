package com.zkytech.spark.operator.transfer

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object Spark29_RDD_Operator11 {
  def main(args: Array[String]): Unit = {

    val sparkConf = new SparkConf().setMaster("local[*]").setAppName("File - RDD").set("spark.ui.port","22112")
    val sc = new SparkContext(sparkConf)

    val rdd = sc.makeRDD(List(1,2,3,4,5,6,2,5,6,2),2)
    // TODO 当数据过滤后，发现数据不够均匀，那么可以缩减分区
    val rdd1: RDD[Int] = rdd.filter(num => num % 2 == 0)

    // TODO 如果发现数据分区不合理，也可以缩减分区
    //      coalesce方法默认情况下无法扩大分区，因为默认不会将数据打乱重新组合，扩大分区是没有意义，如果想要扩大分区，那么必须使用shuffle，打乱数据，重新组合
    // TODO 扩大分区可以通过repartition操作完成，该方法其实就是coalesce方法，但肯定调用了shuffle
    // TODO coalesce方法第一个参数表示缩减分区后的分区数量
    // TODO coalesce方法第二个参数表示分区改变时，是否会打乱重新组合数据，默认不打乱
    val rdd2: RDD[Int] = rdd1.coalesce(1)
    rdd2.saveAsTextFile("output")

    sc.stop()
  }
}
