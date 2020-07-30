package com.zkytech.spark.streaming

import org.apache.spark.SparkConf
import org.apache.spark.streaming.dstream.{DStream, ReceiverInputDStream}
import org.apache.spark.streaming.{Duration, Seconds, StreamingContext}

object SparkStreaming09_Window {
  def main(args: Array[String]): Unit = {
    // TODO spark环境
    // 由于监听需要独立的进程，所以这里至少需要设置两个进程
    val sparkConf: SparkConf = new SparkConf().setMaster("local[2]").setAppName("File - RDD").set("spark.ui.port", "22112")
    val ssc = new StreamingContext(sparkConf,Duration(3000)) // 第二个参数 数据采集时间周期
    // TODO 定义Kafka参数
    val ds: ReceiverInputDStream[String] = ssc.socketTextStream("localhost", 9999)
    // TODO 窗口
    val wordDS: DStream[String] = ds.flatMap(_.split(" "))
    val wordToOneDS: DStream[(String, Int)] = wordDS.map((_, 1))
    // TODO 将多个采集周期作为计算的整体
    // 窗口的范围应是采集周期的整数倍
    // 默认滑动窗口的步长为一个采集周期，可通过window的第二个参数进行设置
    // 窗口的计算的周期等同于窗口的滑动的步长
    // 窗口范围的大小和滑动的步长应该都是采集周期的整数倍

    val windowDS: DStream[(String, Int)] = wordToOneDS.window(Seconds(9))
    val result: DStream[(String, Int)] = windowDS.reduceByKey(_ + _)
    result.print()
    ssc.start()

    // 等待采集器的结束，阻塞
    ssc.awaitTermination()
  }

}
