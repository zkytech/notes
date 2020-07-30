package com.zkytech.spark.streaming

import org.apache.spark.SparkConf
import org.apache.spark.streaming.dstream.{DStream, ReceiverInputDStream}
import org.apache.spark.streaming.{Duration, Seconds, StreamingContext}

object SparkStreaming12_Stop {
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
    // TODO 当业务升级的场合 或 逻辑发生变化
    // TODO Stop方法一般不会放置在main线程完成
    // TODO 需要将stop方法使用新的线程完成调用
    new Thread(new Runnable {
      override def run(): Unit = {
        // TODO Stop方法的调用不应该是线程启动后马上调用
        // TODO Stop方法调用的时机这个时机不容易确定，需要周期性的判断时机是否出现
        while (true){
          Thread.sleep(3000)
          // TODO 关闭时机的判断一般不会使用业务操作
          // TODO 一般采用第三方的程序或存储进行判断
          ssc.stop()
        }
      }
    })
    ssc.awaitTermination()

  }

}
