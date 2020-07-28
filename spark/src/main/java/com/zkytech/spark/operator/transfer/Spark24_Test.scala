package com.zkytech.spark.operator.transfer

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object Spark24_Test {
  def main(args: Array[String]): Unit = {
    // glom => 将每个分区的数据转换为数组
    // 计算所有分区最大值求和（分区内去最大值，分区间最大值求和）
    val sparkConf = new SparkConf().setMaster("local[*]").setAppName("File - RDD").set("spark.ui.port","22112")
    val sc = new SparkContext(sparkConf)
    val fileRDD: RDD[String] = sc.textFile("input/apache.log")
    // 按小时分组查询2015/05/17的数据
    val hourRDD: RDD[(String, Iterable[String])] = fileRDD.map(_.split(" ")(3)).filter(_.substring(0,10)=="17/05/2015").groupBy(_.substring(11, 13)).sortBy(_._1)
    hourRDD.collect().foreach(println)
    sc.stop()
  }
}
