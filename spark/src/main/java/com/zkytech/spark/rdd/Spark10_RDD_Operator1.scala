package com.zkytech.spark.rdd

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object Spark10_RDD_Operator1 {
  def main(args: Array[String]): Unit = {
    val sparkConf = new SparkConf().setMaster("local[*]").setAppName("File - RDD").set("spark.ui.port","22112")
    val sc = new SparkContext(sparkConf)
    // 2个分区 => 12,34
    val rdd: RDD[Int] = sc.makeRDD(List(1, 2, 3, 4),2)
    // TODO 分区问题
    // RDD中有分区列表
    // 默认 分区数量不变，数据会转换后输出
    val rdd1: RDD[Int] = rdd.map(_ * 2)

    rdd1.saveAsTextFile("output")
    sc.stop()
  }
}
