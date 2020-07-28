package com.zkytech.spark.operator.transfer

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object Spark16_RDD_Test {
  def main(args: Array[String]): Unit = {
    val sparkConf = new SparkConf().setMaster("local[*]").setAppName("File - RDD").set("spark.ui.port","22112")
    val sc = new SparkContext(sparkConf)
    // 获取第二个分区的数据
    val dataRDD: RDD[Int] = sc.makeRDD(List(1, 7, 3, 2, 5, 9),3)
    // 获取的分区索引从0开始
    val rdd = dataRDD.mapPartitionsWithIndex((index,iter) => {
      if(index == 1){
        iter
      }else{
        List().iterator
      }

    })
    println(rdd.collect().mkString(","))
    sc.stop()
  }
}
