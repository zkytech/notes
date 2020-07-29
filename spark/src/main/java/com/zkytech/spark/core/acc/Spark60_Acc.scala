package com.zkytech.spark.core.acc

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object Spark60_Acc {
  def main(args: Array[String]): Unit = {

    val sparkConf = new SparkConf().setMaster("local[*]").setAppName("File - RDD").set("spark.ui.port", "22112")
    val sc = new SparkContext(sparkConf)
    val rdd: RDD[Int] = sc.makeRDD(List(1, 2, 3, 4))
//    val sum: Int = rdd.reduce(_ + _)
//    println("sum = " + sum)
    var sum = 0
    rdd.map(("a",_)).foreach{
      case (k,v) => sum += v
    }
    sc.stop()
  }
}
