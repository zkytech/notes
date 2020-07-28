package com.zkytech.spark.operator.action

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object Spark49_RDD_Action03 {
  def main(args: Array[String]): Unit = {

    val sparkConf = new SparkConf().setMaster("local[*]").setAppName("File - RDD").set("spark.ui.port", "22112")
    val sc = new SparkContext(sparkConf)
    val rdd: RDD[Int] = sc.makeRDD(List(5, 2, 3, 1),2)
    // TODO sum
    val d: Double = rdd.sum()
    // TODO aggregate
    val i: Int = rdd.aggregate(0)(_ + _, _ + _)
    // TODO fold
    val i1: Int = rdd.fold(10)(_ + _)
    // TODO countByKey
    val rdd1: RDD[(String, Int)] = sc.makeRDD(List(("a", 1), ("a", 1), ("a", 1)))
    val wordToCount: collection.Map[String, Long] = rdd1.countByKey()
    // TODO countByValue
    //
    println(i)
    println(d)
    sc.stop()
  }
}
