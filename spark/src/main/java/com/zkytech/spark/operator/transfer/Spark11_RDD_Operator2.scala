package com.zkytech.spark.operator.transfer

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object Spark11_RDD_Operator2 {
  def main(args: Array[String]): Unit = {
    val sparkConf = new SparkConf().setMaster("local[*]").setAppName("File - RDD").set("spark.ui.port","22112")
    val sc = new SparkContext(sparkConf)
    // 2个分区 => 12,34
    val rdd: RDD[Int] = sc.makeRDD(List(1, 2, 3, 4),2)
    // TODO 分区问题
    // RDD中有分区列表
    // 默认 分区数量不变，数据会转换后输出
    // TODO 分区内数据是按照顺序依次执行，第一条数据所有罗技全部执行完毕之后才会执行下一条数据
    //      分区间数据执行没有顺序，而且无需等待
    val rdd1: RDD[Int] = rdd.map(x=>{
      println("x map 1= " + x)
      x
    })
    val rdd2: RDD[Int] = rdd1.map(x=>{
      println("x map 2= " + x)
      x
    })

    rdd2.collect().mkString("")
    sc.stop()
  }
}
