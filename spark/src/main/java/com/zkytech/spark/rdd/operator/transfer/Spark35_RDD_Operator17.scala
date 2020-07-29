package com.zkytech.spark.rdd.operator.transfer

import org.apache.spark.rdd.RDD
import org.apache.spark.{Partitioner, SparkConf, SparkContext}

object Spark35_RDD_Operator17 {
  def main(args: Array[String]): Unit = {

    val sparkConf = new SparkConf().setMaster("local[*]").setAppName("File - RDD").set("spark.ui.port","22112")
    val sc = new SparkContext(sparkConf)
    // TODO 自定义分区器 - 自己决定数据放置在哪个分区做处理
    val rdd1 = sc.makeRDD(List(
      ("cba","消息1"),("cba","消息2"),("cba","消息3"),
      ("nba","消息4"),("wnba","消息5"),("nba","消息6")
    ),2)
    val rdd2: RDD[(String, String)] = rdd1.partitionBy(new MyPartitioner(3))
    val rdd3: RDD[(Int, (String, String))] = rdd2.mapPartitionsWithIndex((index, datas) => {
      datas.map(data => (index, data))
    })
    rdd3.collect().foreach(println )
    sc.stop()
  }
}
// TODO 自定义分区器
//
// 重写方法
class MyPartitioner(num:Int) extends Partitioner{
  // 获取分区的数量
  override def numPartitions: Int = {
    num
  }
  // 根据数据的key来决定数据放在哪个分区中进行处理
  // 方法的返回值表示分区的编号（索引）
  override def getPartition(key: Any): Int = {
    key match{
      case "nba" => 0
      case _ => 1
    }
  }
}
