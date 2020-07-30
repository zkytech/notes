package com.zkytech.spark.streaming

import org.apache.spark.SparkConf
import org.apache.spark.streaming.dstream.{DStream, ReceiverInputDStream}
import org.apache.spark.streaming.{Duration, Seconds, StreamingContext}

object SparkStreaming10_Window1 {
  def main(args: Array[String]): Unit = {
    // TODO spark环境

    // 由于监听需要独立的进程，所以这里至少需要设置两个进程
    val sparkConf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("File - RDD").set("spark.ui.port", "22112")
    val ssc = new StreamingContext(sparkConf,Duration(3000)) // 第二个参数 数据采集时间周期
    ssc.sparkContext.setCheckpointDir("checkpoint")

    // TODO 定义Kafka参数
    val ds: ReceiverInputDStream[String] = ssc.socketTextStream("localhost", 9999)

    // TODO 窗口
    val wordToOneDS: DStream[(String, Int)] = ds.map(num => ("key", num.toInt))
    // reduceByKeyAndWindow 方法一般用于重复数据的范围比较大的场合，这样可以优化效率
    val result: DStream[(String, Int)] = wordToOneDS.reduceByKeyAndWindow(
      (x, y) =>{
        println(s"x=$x,y=$y")
        x + y
      },
      (x, y) =>{
        println(s"a=$x,b=$y")
        x - y

      },
      Seconds(9)
    )
    result.foreachRDD(rdd=>rdd.foreach(println))
    ssc.start()
    // 等待采集器的结束，阻塞
    ssc.awaitTermination()
  }

}
