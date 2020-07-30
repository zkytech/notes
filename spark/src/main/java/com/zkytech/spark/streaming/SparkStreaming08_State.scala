package com.zkytech.spark.streaming

import org.apache.kafka.clients.consumer.{ConsumerConfig, ConsumerRecord}
import org.apache.spark.SparkConf
import org.apache.spark.streaming.dstream.{DStream, InputDStream, ReceiverInputDStream}
import org.apache.spark.streaming.kafka010.{ConsumerStrategies, KafkaUtils, LocationStrategies}
import org.apache.spark.streaming.{Duration, StreamingContext}

object SparkStreaming08_State {
  def main(args: Array[String]): Unit = {
    // TODO spark环境
    // 由于监听需要独立的进程，所以这里至少需要设置两个进程
    val sparkConf: SparkConf = new SparkConf().setMaster("local[2]").setAppName("File - RDD").set("spark.ui.port", "22112")
    val ssc = new StreamingContext(sparkConf,Duration(3000)) // 第二个参数 数据采集时间周期
    // TODO 定义Kafka参数
    val ds: ReceiverInputDStream[String] = ssc.socketTextStream("localhost", 9999)
    // TODO 转换
//    val newDS: DStream[String] = ds.transform(rdd => rdd.map(_*2))  // rdd 的计算 在executor中执行
    val newDS: DStream[String] = ds.map(_ * 2)
    newDS.print()


    ssc.start()
    // TODO 模拟数据发送

    // 等待采集器的结束，阻塞
    ssc.awaitTermination()
  }

}
