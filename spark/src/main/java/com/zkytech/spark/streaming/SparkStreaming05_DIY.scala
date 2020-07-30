package com.zkytech.spark.streaming

import java.io.{BufferedReader, InputStreamReader}
import java.net.Socket

import org.apache.spark.SparkConf
import org.apache.spark.storage.StorageLevel
import org.apache.spark.streaming.dstream.{DStream, ReceiverInputDStream}
import org.apache.spark.streaming.receiver.Receiver
import org.apache.spark.streaming.{Duration, StreamingContext}

object SparkStreaming05_DIY {
  def main(args: Array[String]): Unit = {
    // TODO spark环境
    // 由于监听需要独立的进程，所以这里至少需要设置两个进程
    val sparkConf: SparkConf = new SparkConf().setMaster("local[2]").setAppName("File - RDD").set("spark.ui.port", "22112")
    val ssc = new StreamingContext(sparkConf,Duration(3000)) // 第二个参数 数据采集时间周期
    // TODO 执行逻辑
    val ds: ReceiverInputDStream[String] = ssc.receiverStream(new MyReceiver("localhost", 9999)  )
    ds.print()
    // TODO 关闭

    ssc.start()
    // TODO 模拟数据发送

    // 等待采集器的结束，阻塞
    ssc.awaitTermination()
  }

  // TODO 自定义数据采集器
  // 1. 继承Receiver，定义泛型
  //      Reveiver的构造方法有参数，所以子类在继承时，应该传递这个参数
  // 2. 重写方法
  //      onStart
  //      onStop

  class MyReceiver(
                    host: String,
                    port: Int,
                  ) extends Receiver[String](StorageLevel.MEMORY_ONLY){

    private var socket: Socket = _
    def receive(): Unit ={
      val reader = new BufferedReader(
        new InputStreamReader(
          socket.getInputStream,
          "UTF-8"
        )
      )
      var s:String = null
      while(true){
        s = reader.readLine()
        if(s != null){
          // TODO 将获取的数据保存到框架内进行封装
          store(s)
        }
      }
    }
    override def onStart(): Unit = {
      socket = new Socket(host, port)
      new Thread("Socket Receiver") {
        setDaemon(true)
        override def run() { receive() }
      }.start()

    }

    override def onStop(): Unit = {

      socket.close()
      socket = null

    }
  }
}
