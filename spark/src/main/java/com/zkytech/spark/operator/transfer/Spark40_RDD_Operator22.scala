package com.zkytech.spark.operator.transfer

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object Spark40_RDD_Operator22 {
  def main(args: Array[String]): Unit = {

    val sparkConf = new SparkConf().setMaster("local[*]").setAppName("File - RDD").set("spark.ui.port", "22112")
    val sc = new SparkContext(sparkConf)
    // TODO 将分区内相同Key取最大值，分区相同的key求和
    // 0 => [(a,1),(b,2),(c,3)]
    //                          => [(a,1),(b,6),(c,9)]
    // 1 => [(b,4),(c,6)]
    val rdd: RDD[(String, Int)] = sc.makeRDD(List(
      ("a", 1), ("b", 2), ("c", 3),
      ("b", 4), ("c", 5), ("c", 6)), 2)
    // TODO aggregateByKey 根据key进行数据聚合
    // Scala语法：函数柯里化
    // 方法有两个参数列表需要传递参数
    // 第一个参数列表中传递参数为zeroValue，为计算的初始值
    //
    // 第二个参数列表中传递参数为
    //    seqOp:  分区内的计算规则
    //    comOp:  分区间的计算规则
    val rdd1: RDD[(String, Int)] = rdd.aggregateByKey(0)(
      (x, y) => math.max(x, y),
      (x, y) => x + y
    )
    // 如果分区内计算规则和分区间计算规则相同，那么可以将aggregateByKey简化为另外一个方法foldByKey
    val result: RDD[(String, Int)] = rdd.foldByKey(0)(_ + _)
    println(result.collect().mkString(","))
    println(rdd1.collect().mkString(","))

    sc.stop()
  }
}
