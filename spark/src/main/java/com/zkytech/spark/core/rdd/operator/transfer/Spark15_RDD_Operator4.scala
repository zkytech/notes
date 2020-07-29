package com.zkytech.spark.core.rdd.operator.transfer

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object Spark15_RDD_Operator4 {
  def main(args: Array[String]): Unit = {
    val sparkConf = new SparkConf().setMaster("local[*]").setAppName("File - RDD").set("spark.ui.port","22112")
    val sc = new SparkContext(sparkConf)
    // 获取每个分区最大值以及分区号
    val dataRDD: RDD[Int] = sc.makeRDD(List(1, 7, 3, 2, 5, 9),3)

    val rdd = dataRDD.mapPartitionsWithIndex((index,iter) => {
      List((index,iter.max)).iterator
    })
    println(rdd.collect().mkString(","))
    sc.stop()
  }
}
