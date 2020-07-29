package com.zkytech.spark.core.acc

import org.apache.spark.rdd.RDD
import org.apache.spark.util.{AccumulatorV2, LongAccumulator}
import org.apache.spark.{SparkConf, SparkContext}

import scala.collection.mutable

object Spark63_Acc3 {
  def main(args: Array[String]): Unit = {

    val sparkConf = new SparkConf().setMaster("local[*]").setAppName("File - RDD").set("spark.ui.port", "22112")
    val sc = new SparkContext(sparkConf)
    val rdd: RDD[String] = sc.makeRDD(List("hello spark","hello scala"))
    // TODO 累加器:  wordCount

    // TODO 1.创建累加器
    val acc = new MyWordCountAccumulator
    // TODO 2.注册累加器
    sc.register(acc)
    // TODO 3.使用累加器
    rdd.flatMap(_.split(" ")).foreach{
      case v => acc.add(v)
    }
    // TODO 4.获取累加器的值
    println(acc.value)
    sc.stop()
  }
}

// TODO 自定义累加器
// 1. 继承AccumulatorV2,定义泛型
//      IN: 累加器输入值的类型
//      OUT: 累加器返回结果的类型
class MyWordCountAccumulator extends AccumulatorV2[String,mutable.Map[String,Int]]{
  // 存储wordCount的集合
  var wordCountMap = mutable.Map[String,Int]()
  // 累加器是否初始化
  override def isZero: Boolean = wordCountMap.isEmpty
  // 复制累加器
  override def copy(): AccumulatorV2[String, mutable.Map[String, Int]] = {
    new MyWordCountAccumulator
  }
  // 重置累加器
  override def reset(): Unit = {
    wordCountMap.clear()
  }


  override def add(v: String): Unit = {
    wordCountMap.update(v,wordCountMap.getOrElse(v,0) + 1)
  }
  // 合并当前累加器和其他累加器
  override def merge(other: AccumulatorV2[String, mutable.Map[String, Int]]): Unit = {
    val map1 = wordCountMap
    val map2 = other.value
    wordCountMap = map1.foldLeft(map2)(
      (map, kv) => {
        map(kv._1) = map.getOrElse(kv._1, 0) + kv._2
        map
      }
    )

  }
  // 返回累加器的值
  override def value: mutable.Map[String, Int] = wordCountMap
}