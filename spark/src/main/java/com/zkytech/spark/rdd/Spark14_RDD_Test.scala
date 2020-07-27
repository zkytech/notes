package com.zkytech.spark.rdd

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object Spark14_RDD_Test {
  def main(args: Array[String]): Unit = {
    // TODO Spark - RDD - 算子（方法）
    val sparkConf = new SparkConf().setMaster("local[*]").setAppName("File - RDD").set("spark.ui.port","22112")
    val sc = new SparkContext(sparkConf)
    val dataRDD: RDD[Int] = sc.makeRDD(List(1, 7, 3, 2, 5, 6),2)
    // 获取每个数据分区的最大值
    val rdd: RDD[Int] = dataRDD.mapPartitions(iter => {
      List(iter.max).iterator
    })
    println(rdd.collect().mkString(","))
    sc.stop()
  }
}
