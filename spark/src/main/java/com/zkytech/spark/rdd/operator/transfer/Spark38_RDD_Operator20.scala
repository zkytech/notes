package com.zkytech.spark.rdd.operator.transfer

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object Spark38_RDD_Operator20 {
  def main(args: Array[String]): Unit = {

    val sparkConf = new SparkConf().setMaster("local[*]").setAppName("File - RDD").set("spark.ui.port","22112")
    val sc = new SparkContext(sparkConf)
    // TODO groupByKey： 根据数据的key进行分组
    // groupBy： 根据指定的规则对数据进行分组
    val rdd: RDD[(String, Int)] = sc.makeRDD(List(("hello", 1), ("scala", 1), ("hello", 1)))
    // TODO 调用groupByKey后，返回数据的类型为元组
    //    元组的第一个元素表示的是用于分组的key
    //    元组的第二个元素表示的是分组后，相同的key的value的集合
    // groupByKey 是面向在哼歌数据集，而不是某一个分区
    // groupByKey 对一个分区的数据进行分组后不能继续执行后续操作，需要等待其他分区的数据全部到达后，才能执行后续计算，
    // 但如果在内存中等待，那么可能由于内存不够，导致执行失败，所以这个等待的过程应该依靠磁盘文件。
    // 一个分区就是一个Task，如果处理过程中存在shuffle操作，那么就会将task一分为二
    val groupRDD: RDD[(String, Iterable[Int])] = rdd.groupByKey()
    val wordToCount: RDD[(String, Int)] = groupRDD.map {
      case (word, iter) => {
        (word, iter.size)
      }
    }

    println(wordToCount.collect().mkString(","))

    sc.stop()
  }
}
