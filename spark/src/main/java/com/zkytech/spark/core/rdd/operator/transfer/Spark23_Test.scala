package com.zkytech.spark.core.rdd.operator.transfer

import org.apache.spark.{SparkConf, SparkContext}

object Spark23_Test {
  def main(args: Array[String]): Unit = {
    // glom => 将每个分区的数据转换为数组
    // 计算所有分区最大值求和（分区内去最大值，分区间最大值求和）
    val sparkConf = new SparkConf().setMaster("local[*]").setAppName("File - RDD").set("spark.ui.port","22112")
    val sc = new SparkContext(sparkConf)

    val dataRDD = sc.makeRDD(List("Hello","hive","hbase","Hadoop"),3)
    println(dataRDD.groupBy(_ (0)).collect().mkString(","))

    sc.stop()
  }
}
