package com.zkytech.spark.core.rdd.operator.transfer

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object Spark28_RDD_Operator10 {
  def main(args: Array[String]): Unit = {

    val sparkConf = new SparkConf().setMaster("local[*]").setAppName("File - RDD").set("spark.ui.port","22112")
    val sc = new SparkContext(sparkConf)

    val rdd = sc.makeRDD(List(1,2,3,4,5,6,2,5,6,2),3)
    // TODO distinct 去重
    val rdd1: RDD[Int] = rdd.distinct()
    println(rdd1.collect().mkString(","))
    sc.stop()
  }
}
