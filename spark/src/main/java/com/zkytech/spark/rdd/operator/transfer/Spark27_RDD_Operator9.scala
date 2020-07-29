package com.zkytech.spark.rdd.operator.transfer

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object Spark27_RDD_Operator9 {
  def main(args: Array[String]): Unit = {
    // glom => 将每个分区的数据转换为数组

    val sparkConf = new SparkConf().setMaster("local[*]").setAppName("File - RDD").set("spark.ui.port","22112")
    val sc = new SparkContext(sparkConf)

    val dataRDD = sc.makeRDD(List(1,2,3,4,5,6),3)
    // TODO sample用于从数据集中抽取数据
    // 第一个参数表示数据抽取后是否返回，可以重复抽取
    //    true:抽取后放回
    //    false:抽取后不放回
    // 第二个参数表示数据抽取的几率（不放回的场合），重复抽取的次数（放回的场合）
    //    这里的几率不是能够被抽取的数据总量的比率
    // 第三个参数表示随机数种子，可以确定数据的抽取

    val rdd: RDD[Int] = dataRDD.sample(false, 0.5,1) // 指定了固定的随机数种子之后无论运行多少次，输出的结果都相同
    val rdd1: RDD[Int] = dataRDD.sample(true, 2)
    println(rdd.collect().mkString(","))
    println(rdd1.collect().mkString(","))
    sc.stop()
  }
}
