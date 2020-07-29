package com.zkytech.spark.rdd.operator.action

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object Spark45_RDD_Action00 {
  def main(args: Array[String]): Unit = {

    val sparkConf = new SparkConf().setMaster("local[*]").setAppName("File - RDD").set("spark.ui.port", "22112")
    val sc = new SparkContext(sparkConf)
    val rdd: RDD[Int] = sc.makeRDD(List(1, 2, 3, 4))
    // TODO Spark算子 - 行动
    // 行动算子，其实不会再产生新的RDD而是处罚作业的执行，
    // 行动算子执行后，会获取到作业的执行结果。
    // 转化内算子不会触发作业的执行，只是功能的扩展和包装
    // Spark的行动算子执行时，会产生Job对象，然后提交这个Job对象
    val data: Array[Int] = rdd.collect()
    data.foreach(println)

    sc.stop()
  }
}
