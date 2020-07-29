package com.zkytech.spark.rdd.operator.action

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object Spark47_RDD_Action02 {
  def main(args: Array[String]): Unit = {

    val sparkConf = new SparkConf().setMaster("local[*]").setAppName("File - RDD").set("spark.ui.port", "22112")
    val sc = new SparkContext(sparkConf)
    val rdd: RDD[Int] = sc.makeRDD(List(5, 2, 3, 1))
    // TODO takeOrdered
    val ints: Array[Int] = rdd.takeOrdered(3)
    println(ints.mkString(","))
    sc.stop()
  }
}
