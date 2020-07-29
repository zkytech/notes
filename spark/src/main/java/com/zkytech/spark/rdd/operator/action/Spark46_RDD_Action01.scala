package com.zkytech.spark.rdd.operator.action

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object Spark46_RDD_Action01 {
  def main(args: Array[String]): Unit = {

    val sparkConf = new SparkConf().setMaster("local[*]").setAppName("File - RDD").set("spark.ui.port", "22112")
    val sc = new SparkContext(sparkConf)
    val rdd: RDD[Int] = sc.makeRDD(List(1, 2, 3, 4))
    // TODO reduce
    // 简化、规约
    val result: Int = rdd.reduce(_ + _)
    println(result)
    // TODO collect
    // 采集数据
    // collect方法会将所有分区计算的结果拉去到当前节点的内存中，可能会出现内存溢出
//    val result: Array[Int] = rdd.collect()
    // TODO count
    val count: Long = rdd.count()
    // TODO first
    val first: Int = rdd.first()
    // TODO take
    val subArray: Array[Int] = rdd.take(3)
    sc.stop()
  }
}
