package com.zkytech.spark.core.rdd.operator.transfer

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object Spark12_RDD_Test {
  def main(args: Array[String]): Unit = {
    val sparkConf = new SparkConf().setMaster("local[*]").setAppName("File - RDD").set("spark.ui.port","22112")
    val sc = new SparkContext(sparkConf)
    val rdd: RDD[Int] = sc.makeRDD(List(1, 2, 3, 4),2)
    // TODO 从服务器日志数据apache.log中获取用户请求URL资源路径
    val fileRDD: RDD[String] = sc.textFile("input/apache.log")
    val urlRDD: RDD[String] = fileRDD.map(line => {
      line.split(" ")(6)
    })
    urlRDD.collect().foreach(println)
    sc.stop()
  }
}
