package com.zkytech.spark.rdd.dep

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object Spark53_Serial1 {
  def main(args: Array[String]): Unit = {

    val sparkConf = new SparkConf().setMaster("local[*]").setAppName("File - RDD").set("spark.ui.port", "22112")
    val sc = new SparkContext(sparkConf)
    val rdd: RDD[String] = sc.makeRDD(List("hello scala", "hello spark"))
    // 依赖关系中，现在的数据分区和依赖前的数据分区1对1
    println(rdd.dependencies)
    println("-------------------------")
    val wordRDD: RDD[String] = rdd.flatMap(
      string => {
        string.split(" ")
      }
    )
    println(wordRDD.dependencies)
    println("-------------------------")
    val mapRDD: RDD[(String, Int)] = wordRDD.map((_, 1))
    println(mapRDD.dependencies)
    println("-------------------------")
    // 如果Spark的计算过程中某一个节点计算失败，那么框架会尝试重新计算
    // Spark既然想要重新计算，那么就需要知道数据的来源，并且还要知道数据经历了那些计算
    // RDD不保存计算的数据，但是会保存元数据的信息
    val result: RDD[(String, Int)] = mapRDD.reduceByKey(_ + _)
    println(result.dependencies)
    println("-------------------------")
    println(result.collect().mkString(","))

    sc.stop()
  }
}
