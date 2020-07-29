package com.zkytech.spark.rdd.operator.transfer

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object Spark01_RDD_Memory {
  def main(args: Array[String]): Unit = {
    val sparkConfig = new SparkConf().setMaster("local").setAppName("wordCount").set("spark.ui.port","22112")
    val sc = new SparkContext(sparkConfig)
    // TODO Spark - 从内存中创建RDD
    // 1.parallelize ：并行
    val list = List(1,2,3,4)
    sc.parallelize(list).collect().foreach(print)
    println()
    // 2.makeRDD的底层代码其实就是调用了parallelize方法
    sc.makeRDD(list).collect().foreach(print)
    // TODO 设定并行度（分区的数量）
    // 并行度默认会从spark配置信息中获取spark.default.parallelism值，如果获取不到指定的参数，会采用默认值totalCores(机器的总核数)
    // 机器总核数 = 当前环境总可用核数
    // local => 单核（单线程） => 1
    // local[4] => 4核心（4个线程） => 4
    // local[*] => 最大核数
    // TODO 内存中的集合数据会按照平均分的方式进行分区处理
    // TODO saveAsTextFile方法如果文件路径已经存在，会发生错误
    val rdd: RDD[Int] = sc.makeRDD(List(1, 2, 3, 4), 2)
    rdd.saveAsTextFile("output")

    // TODO 内存中的集合数据如果不能平均分，会将多余的数据放在最后一个分区
    val rdd1: RDD[Int] = sc.makeRDD(list, 3)
    // 将RDD处理后的数据保存到分区文件中
    rdd1.saveAsTextFile("output1")

    // TODO 内存中数据的分区基本上就是平均分，如果不能整除，会采用一个基本的算法实现分配
    val rdd2: RDD[Int] = sc.makeRDD(List(1, 2, 3, 4, 5), 3)
    rdd2.saveAsTextFile("output2")
  }

}
