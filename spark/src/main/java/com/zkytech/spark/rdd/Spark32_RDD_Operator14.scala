package com.zkytech.spark.rdd

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object Spark32_RDD_Operator14 {
  def main(args: Array[String]): Unit = {

    val sparkConf = new SparkConf().setMaster("local[*]").setAppName("File - RDD").set("spark.ui.port","22112")
    val sc = new SparkContext(sparkConf)

    val rdd = sc.makeRDD(List(1,2,3,4,5,6,2,5,6,2),2)
    // TODO sortBy

    // 默认排序规则为 升序
    // sortBy可以通过传递第二个参数改变排序的方式
    // sortBy可以设定第三个参数改变分区
    val rdd1: RDD[Int] = rdd.sortBy(num => num,true)
    //
    sc.stop()
  }
}
