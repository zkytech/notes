package com.zkytech.spark.streaming

import java.io.{BufferedReader, InputStreamReader}
import java.net.Socket

import org.apache.commons.codec.StringDecoder
import org.apache.kafka.clients.consumer.{ConsumerConfig, ConsumerRecord}
import org.apache.spark.SparkConf
import org.apache.spark.storage.StorageLevel
import org.apache.spark.streaming.dstream.{DStream, InputDStream, ReceiverInputDStream}
import org.apache.spark.streaming.kafka010.{ConsumerStrategies, KafkaUtils, LocationStrategies}
import org.apache.spark.streaming.receiver.Receiver
import org.apache.spark.streaming.{Duration, StreamingContext}

object SparkStreaming06_Kafka {
  def main(args: Array[String]): Unit = {
    // TODO spark环境
    // 由于监听需要独立的进程，所以这里至少需要设置两个进程
    val sparkConf: SparkConf = new SparkConf().setMaster("local[2]").setAppName("File - RDD").set("spark.ui.port", "22112")
    val ssc = new StreamingContext(sparkConf,Duration(3000)) // 第二个参数 数据采集时间周期
    // TODO 定义Kafka参数
    val kafkaPara: Map[String, Object] = Map[String, Object](
      ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG -> "zk0:9092,zk1:9092,zk2:9092",
      ConsumerConfig.GROUP_ID_CONFIG -> "atguigu",
      "key.deserializer" -> "org.apache.kafka.common.serialization.StringDeserializer",
      "value.deserializer" -> "org.apache.kafka.common.serialization.StringDeserializer"
    )


    // TODO 使用SparkStreaming读取Kafka的数据
    val kafkaDStream: InputDStream[ConsumerRecord[String, String]] = KafkaUtils.createDirectStream[String, String](ssc,
      LocationStrategies.PreferConsistent,
      ConsumerStrategies.Subscribe[String, String](Set("atguigu"), kafkaPara))


    val valueDStream: DStream[String] = kafkaDStream.map((record: ConsumerRecord[String, String]) => record.value())

    valueDStream.flatMap(_.split(" "))
      .map((_,1))
      .reduceByKey(_+_)
      .print()
    // TODO 关闭

    ssc.start()
    // TODO 模拟数据发送

    // 等待采集器的结束，阻塞
    ssc.awaitTermination()
  }

}
