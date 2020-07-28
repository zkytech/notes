package com.zkytech.spark.operator.transfer

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object Spark36_RDD_Operator18 {
  def main(args: Array[String]): Unit = {

    val sparkConf = new SparkConf().setMaster("local[*]").setAppName("File - RDD").set("spark.ui.port","22112")
    val sc = new SparkContext(sparkConf)
    // TODO reduceByKey： 根据数据的key进行分组，然后对value进行聚合
    val rdd: RDD[(String, Int)] = sc.makeRDD(List(("hello", 1), ("scala", 1), ("hello", 1)))
    // reduceByKey 第一个参数表示相同key的value的聚合方式
    // reduceByKey 第二个参数表示聚合后的分区数量
    val rdd1: RDD[(String, Int)] = rdd.reduceByKey(_ + _)
    val rdd2: RDD[(String, Int)] = rdd.reduceByKey(_ + _, 2)
    println(rdd1.collect().mkString(","))
    println(rdd2.collect().mkString(","))

    sc.stop()
  }
}
