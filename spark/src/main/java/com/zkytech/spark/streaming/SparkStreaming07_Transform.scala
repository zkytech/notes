package com.zkytech.spark.streaming

import org.apache.kafka.clients.consumer.{ConsumerConfig, ConsumerRecord}
import org.apache.spark.SparkConf
import org.apache.spark.streaming.dstream.{DStream, InputDStream, ReceiverInputDStream}
import org.apache.spark.streaming.kafka010.{ConsumerStrategies, KafkaUtils, LocationStrategies}
import org.apache.spark.streaming.{Duration, StreamingContext}

object SparkStreaming07_Transform {
  def main(args: Array[String]): Unit = {
    // TODO spark环境
    // 由于监听需要独立的进程，所以这里至少需要设置两个进程
    val sparkConf: SparkConf = new SparkConf().setMaster("local[2]").setAppName("File - RDD").set("spark.ui.port", "22112")
    val ssc = new StreamingContext(sparkConf,Duration(3000)) // 第二个参数 数据采集时间周期
    ssc.sparkContext.setCheckpointDir("checkpoint")
    // TODO 定义Kafka参数
    val ds: ReceiverInputDStream[String] = ssc.socketTextStream("localhost", 9999)
    // TODO 转换
    // 数据的有状态的保存
    // 将spark每个采集周期数据的处理结果保存起来，然后和后续的数据进行聚合
    // reduceByKey方法是无状态的，而我们需要的是有状态的数据操作
    // updateStateByKey是有状态计算方法
    //  第一个参数表示 相同key的value的集合
    //  第二个参数表示 缓冲区数据
    ds.flatMap(_.split(" "))
      .map((_,1))
      .updateStateByKey(
        (seq: Seq[Int], buffer: Option[Long]) => {
          println("seq:"+seq.mkString(","))
          println("buffer:"+buffer.getOrElse("null"))
          val newBufferSum: Long = buffer.getOrElse(0L) + seq.sum
          Option(newBufferSum)
        }
      ).print()



    ssc.start()
    // TODO 模拟数据发送

    // 等待采集器的结束，阻塞
    ssc.awaitTermination()
  }

}
