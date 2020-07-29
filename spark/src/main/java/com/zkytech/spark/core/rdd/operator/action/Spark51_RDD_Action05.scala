package com.zkytech.spark.core.rdd.operator.action

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object Spark51_RDD_Action05 {
  def main(args: Array[String]): Unit = {

    val sparkConf = new SparkConf().setMaster("local[*]").setAppName("File - RDD").set("spark.ui.port", "22112")
    val sc = new SparkContext(sparkConf)
    val rdd: RDD[Int] = sc.makeRDD(List(1,2,3,4))
    // TODO foreach 方法
    rdd.collect().foreach(println)
    // TODO foreach 算子
    // rdd的方法称之为算子
    rdd.foreach(println)
    sc.stop()
  }
}
