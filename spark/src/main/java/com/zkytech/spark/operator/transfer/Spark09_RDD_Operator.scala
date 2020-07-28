package com.zkytech.spark.operator.transfer

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object Spark09_RDD_Operator {
  def main(args: Array[String]): Unit = {
    val sparkConf = new SparkConf().setMaster("local[*]").setAppName("File - RDD").set("spark.ui.port","22112")
    val sc = new SparkContext(sparkConf)

    // 能够将旧的RDD通过方法转换为新的RDD，但是不会出发作业的执行
    val rdd: RDD[Int] = sc.makeRDD(List(1, 2, 3, 4))
    // 转换:旧RDD => 算子 => 新RDD
    val rdd1: RDD[Int] = rdd.map(_ * 2)
    // 读取数据
    rdd1.collect()
    sc.stop()
  }
}
