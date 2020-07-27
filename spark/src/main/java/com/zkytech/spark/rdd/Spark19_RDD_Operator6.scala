package com.zkytech.spark.rdd

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object Spark19_RDD_Operator6 {
  def main(args: Array[String]): Unit = {
    // glom => 将每个分区的数据转换为数组

    val sparkConf = new SparkConf().setMaster("local[*]").setAppName("File - RDD").set("spark.ui.port","22112")
    val sc = new SparkContext(sparkConf)

    val dataRDD = sc.makeRDD(List(1,2,3,4,5,6),2)
    val rdd: RDD[Array[Int]] = dataRDD.glom()
    rdd.foreach(
      array=>{
        println(array.mkString(","))
      }
    )

    sc.stop()
  }
}
