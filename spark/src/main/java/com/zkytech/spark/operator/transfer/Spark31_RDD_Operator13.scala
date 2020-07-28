package com.zkytech.spark.operator.transfer

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object Spark31_RDD_Operator13 {
  def main(args: Array[String]): Unit = {

    val sparkConf = new SparkConf().setMaster("local[*]").setAppName("File - RDD").set("spark.ui.port","22112")
    val sc = new SparkContext(sparkConf)

    val rdd = sc.makeRDD(List(1,2,3,4,5,6,2,5,6,2),2)
    // TODO 当数据过滤后，发现数据不够均匀，那么可以缩减分区
    val rdd1: RDD[Int] = rdd.filter(num => num % 2 == 0)

    // TODO 缩减分区：coalesce
    val rdd2: RDD[Int] = rdd1.coalesce(1)
    // TODO 扩大分区：repartition
    // 从底层源码的角度看，repartition其实就是coalesce，并且肯定进行shuffle操作
    rdd2.saveAsTextFile("output")

    sc.stop()
  }
}
