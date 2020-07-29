package com.zkytech.spark.core.rdd.operator.transfer

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object Spark33_RDD_Operator15 {
  def main(args: Array[String]): Unit = {

    val sparkConf = new SparkConf().setMaster("local[*]").setAppName("File - RDD").set("spark.ui.port","22112")
    val sc = new SparkContext(sparkConf)

    val rdd1 = sc.makeRDD(List(1,2,3,5),2)
    val rdd2: RDD[Int] = sc.makeRDD(List(6, 7, 8, 9), 2)
    // TODO 并集 数据合并，分区也会合并
    val rdd3: RDD[Int] = rdd1.union(rdd2)
    println(rdd3.collect().mkString(","))
    // TODO 交集 保留最大分区数，数据被打乱重组，shuffle
    val rdd4: RDD[Int] = rdd1.intersection(rdd2)
    println(rdd4.collect().mkString(","))
    // TODO 差集 以调用差集方法的rdd分区数为准 数据被打乱重组，shuffle
    val rdd5: RDD[Int] = rdd1.subtract(rdd2)
    println(rdd5.collect().mkString(","))
    // TODO 拉链 分区数不变
    val rdd6: RDD[(Int, Int)] = rdd1.zip(rdd2)
    println(rdd6.collect().mkString(","))

    sc.stop()
  }
}
