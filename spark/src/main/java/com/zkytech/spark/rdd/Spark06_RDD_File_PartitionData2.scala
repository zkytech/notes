package com.zkytech.spark.rdd

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object Spark06_RDD_File_PartitionData2 {
  def main(args: Array[String]): Unit = {
    val sparkConfig = new SparkConf().setMaster("local").setAppName("wordCount").set("spark.ui.port","22112")
    val sc = new SparkContext(sparkConfig)
    // TODO hadoop分区是以文件为单位进行划分的
    //      读取数据不能跨越文件
    // 12/3 = 4
    // 1.txt => (0,0 + 4)
    //       => (4,4 + 4)
    // 2.txt => (0,4 + 4)
    //       => (4,4 + 4)
    val fileRDD1: RDD[String] = sc.textFile("input", 3)

    fileRDD1.saveAsTextFile("output")
    sc.stop()
  }
}
