package com.zkytech.spark.rdd

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object Spark18_Test {
  def main(args: Array[String]): Unit = {
    val sparkConf = new SparkConf().setMaster("local[*]").setAppName("File - RDD").set("spark.ui.port","22112")
    val sc = new SparkContext(sparkConf)
    // 扁平化操作
    val dataRDD = sc.makeRDD(List(List(1, 7, 3),7,List( 2, 5, 9)))
    val dataRDD1: RDD[Int] = dataRDD.flatMap(data =>{
      data match{
        case list:List[Int] => list
        case d:Int => List(d)
      }
    })


    println(dataRDD1.collect().mkString(","))
    sc.stop()
  }
}
