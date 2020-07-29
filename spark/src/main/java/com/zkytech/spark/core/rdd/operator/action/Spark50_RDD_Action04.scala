package com.zkytech.spark.core.rdd.operator.action

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object Spark50_RDD_Action04 {
  def main(args: Array[String]): Unit = {

    val sparkConf = new SparkConf().setMaster("local[*]").setAppName("File - RDD").set("spark.ui.port", "22112")
    val sc = new SparkContext(sparkConf)
    val rdd: RDD[Int] = sc.makeRDD(List(5, 2, 3, 1),2)
    rdd.saveAsTextFile("output")
    rdd.saveAsObjectFile("output1")
    rdd.map((_,1)).saveAsSequenceFile("output2")
    sc.stop()
  }
}
