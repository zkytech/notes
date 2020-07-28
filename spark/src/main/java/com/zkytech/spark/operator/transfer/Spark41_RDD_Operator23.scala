package com.zkytech.spark.operator.transfer

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object Spark41_RDD_Operator23 {
  def main(args: Array[String]): Unit = {

    val sparkConf = new SparkConf().setMaster("local[*]").setAppName("File - RDD").set("spark.ui.port", "22112")
    val sc = new SparkContext(sparkConf)
    val rdd: RDD[(String, Int)] = sc.makeRDD(List(
      ("a", 1), ("b", 2), ("c", 3),
      ("b", 4), ("c", 5), ("c", 6)), 2)
    // TODO combineByKey
    // TODO 每个Key的平均值：相同key的总和/相同key的数量
    // 计算时需要将value的格式改变，只需要第一个v的结构发生改变即可
    // 如果计算时发现相同key的value不符合计算规则的格式的话，那么选择combineByKey

    // TODO combineByKey方法可以传递3个参数
    //    第一个参数表示的就是将计算的第一个值转化内结构
    //    第二个参数表示分区内的计算规则
    //    第三个参数表示分区间的计算规则
    val rdd1: RDD[(String, (Int, Int))] = rdd.combineByKey(
      v => (v, 1),
      (t: (Int, Int), v) => {
        (t._1 + v, t._2 + 1)
      },
      (t1: (Int, Int), t2: (Int, Int)) => {
        (t1._1 + t2._1, t1._2 + t2._2)
      }
    )
    // 从源码来看， reduceByKey combineByKey foldByKey aggregateByKey 四个方法的底层是完全相同的，只是传入的参数不同
    println(rdd1.collect().mkString(","))

    sc.stop()
  }
}
