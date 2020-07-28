package com.zkytech.spark.operator.transfer

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object Spark21_RDD_Operator8 {
  def main(args: Array[String]): Unit = {
    // glom => 将每个分区的数据转换为数组

    val sparkConf = new SparkConf().setMaster("local[*]").setAppName("File - RDD").set("spark.ui.port","22112")
    val sc = new SparkContext(sparkConf)

    val dataRDD = sc.makeRDD(List(1,2,3,4,5,6),3)
    // TODO 过滤
    // 根据指定的规则对数据进行筛选过滤，满足条件的数据保留，不满足的数据丢弃
    val rdd: RDD[Int] = dataRDD.filter(_ % 2 == 0)
    rdd.collect().foreach(println)

    sc.stop()
  }
}
