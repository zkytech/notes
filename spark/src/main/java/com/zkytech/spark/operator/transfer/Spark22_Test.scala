package com.zkytech.spark.operator.transfer

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object Spark22_Test {
  def main(args: Array[String]): Unit = {
    // glom => 将每个分区的数据转换为数组
    // 计算所有分区最大值求和（分区内去最大值，分区间最大值求和）
    val sparkConf = new SparkConf().setMaster("local[*]").setAppName("File - RDD").set("spark.ui.port","22112")
    val sc = new SparkContext(sparkConf)

    val dataRDD = sc.makeRDD(List(1,2,3,4,5,6),3)
    val glomRDD: RDD[Array[Int]] = dataRDD.glom()
    // 将数组中最大值取出
    val maxRDD: RDD[Int] = glomRDD.map(_.max)
    // 将取出的最大值求和
    println(maxRDD.collect().sum)
    sc.stop()
  }
}
