package com.zkytech.spark.core.rdd.persist

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object Spark55_Persist {
  def main(args: Array[String]): Unit = {
    val sparkConf = new SparkConf().setMaster("local[*]").setAppName("File - RDD").set("spark.ui.port", "22112")
    val sc = new SparkContext(sparkConf)
    val rdd: RDD[Int] = sc.makeRDD(List(1, 2, 3, 4))
    val mapRDD: RDD[(Int, Int)] = rdd.map(num => {
      println("map.....")
      (num, 1)
    })
    // 将计算结果进行缓存,重复使用，提高效率
    // 默认的缓存是存储在Executor端的内存中，数据量大的时候，该如何处理？
    // TODO 缓存cache底层调用的就是persist方法
    // persist方法在持久化数据时，会采用不同的存储级别对数据进行持久化操作
    // cache缓存的默认操作就是将数据保存到内存
    // cache存储的数据在内存中，如果内存不够用，executor可以将内存的数据进行整理，然后可以丢弃数据
    // 如果由于executor端整理内存导致缓存的数据丢失，那么数据操作依然要重新执行
    // 如果cache后的数据从头执行数据操作的话，那么必须要遵循血缘关系，所以cache操作不能删除血缘关系
    // cache操作在行动算子执行后，会在血缘关系中增加缓存相关的依赖
    // cache操作不会切断血缘，一旦发生错误，可以重新执行
    mapRDD.cache()
    println(mapRDD.collect().mkString(","))
    println("--------------------------")
    mapRDD.saveAsTextFile("output")
    sc.stop()
  }
}
