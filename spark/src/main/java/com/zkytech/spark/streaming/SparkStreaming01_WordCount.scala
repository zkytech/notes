package com.zkytech.spark.streaming

import org.apache.spark.rdd.RDD
import org.apache.spark.streaming.dstream.{DStream, ReceiverInputDStream}
import org.apache.spark.streaming.{Duration, StreamingContext}
import org.apache.spark.{SparkConf, SparkContext}

object SparkStreaming01_WordCount {
  def main(args: Array[String]): Unit = {
    // TODO spark环境
    val sparkConf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("File - RDD").set("spark.ui.port", "22112")
    val ssc = new StreamingContext(sparkConf,Duration(3000)) // 第二个参数 数据采集时间周期
    // TODO 执行逻辑
    // 从socket获取数据，一行一行获取的
    val socketDS: ReceiverInputDStream[String] = ssc.socketTextStream("localhost", 9999) // 监听地址
    val result: DStream[(String, Int)] = socketDS.flatMap(_.split(" ")).map((_, 1)).reduceByKey(_ + _)
    result.print()
    // TODO 关闭
    // Driver程序执行streaming处理过程中不能结束
    // 采集器在正常情况下启动后不应该停止，除非特殊的情况
    //    ssc.stop()
    // 启动采集器
    ssc.start()
    // 等待采集器的结束
    ssc.awaitTermination()
  }
}
