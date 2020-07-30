package com.zkytech.spark.streaming

import org.apache.spark.SparkConf
import org.apache.spark.rdd.RDD
import org.apache.spark.streaming.dstream.{DStream, InputDStream}
import org.apache.spark.streaming.{Duration, StreamingContext}

import scala.collection.mutable

object SparkStreaming04_File {
  def main(args: Array[String]): Unit = {
    // TODO spark环境
    // 由于监听需要独立的进程，所以这里至少需要设置两个进程
    val sparkConf: SparkConf = new SparkConf().setMaster("local[2]").setAppName("File - RDD").set("spark.ui.port", "22112")
    val ssc = new StreamingContext(sparkConf,Duration(3000)) // 第二个参数 数据采集时间周期
    // TODO 执行逻辑
    // 只会接收时间较新的文件，将旧文件拖入目录是不会有任何反应
    val dirDS: DStream[String] = ssc.textFileStream("in")
    dirDS.print()
    // TODO 关闭

    ssc.start()
    // TODO 模拟数据发送

    // 等待采集器的结束，阻塞
    ssc.awaitTermination()
  }
}
