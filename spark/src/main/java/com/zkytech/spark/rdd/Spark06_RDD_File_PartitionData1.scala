package com.zkytech.spark.rdd

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object Spark06_RDD_File_PartitionData1 {
  def main(args: Array[String]): Unit = {
    val sparkConfig = new SparkConf().setMaster("local").setAppName("wordCount").set("spark.ui.port","22112")
    val sc = new SparkContext(sparkConfig)
    val fileRDD1: RDD[String] = sc.textFile("input/w.txt", 4)
    // TODO Spark - 从磁盘（File）中创建RDD
    // TODO 1. 分几个区
    // 10 byte /  4 = 2 byte ... 2 byte => 5
    // TODO 2. 数据（回车和换行分别算一个字节）如何存储？
    // 0 => (0,2)
    // 1 => (2,4)
    // 2 => (4,6)
    // 3 => (6,8)
    // 4 => (8,10)
    // 数据是以行的方式读取的，但是会考虑偏移量（数据的offset）的设置
    // 1@@ => 012
    // 2@@ => 345
    // 3@@ => 678
    // 4   => 9
    // 0 => (0,2) => 1@@
    // 1 => (2,4) => 2@@
    // 2 => (4,6) => 3@@
    // 3 => (6,8) =>
    // 4 => (8,10)=> 4

    fileRDD1.saveAsTextFile("output")
    sc.stop()
  }
}
