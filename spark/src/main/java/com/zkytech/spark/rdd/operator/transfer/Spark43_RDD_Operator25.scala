package com.zkytech.spark.rdd.operator.transfer

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object Spark43_RDD_Operator25 {
  def main(args: Array[String]): Unit = {

    val sparkConf = new SparkConf().setMaster("local[*]").setAppName("File - RDD").set("spark.ui.port", "22112")
    val sc = new SparkContext(sparkConf)
    val rdd1: RDD[(String, Int)] = sc.makeRDD(List(
      ("a", 1), ("a", 2), ("c", 3)
      ))
    val rdd2: RDD[(String, Int)] = sc.makeRDD(List(
      ("b", 6), ("a", 7), ("a", 5)
    ))
    // join方法可以将两个rdd中相同的key和value连接在一起
    val result: RDD[(String, (Int, Int))] = rdd1.join(rdd2)
    println(result.collect().mkString(","))

    sc.stop()
  }
}
