package com.zkytech.spark.core.rdd.serial

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object Spark52_Serial {
  def main(args: Array[String]): Unit = {

    val sparkConf = new SparkConf().setMaster("local[*]").setAppName("File - RDD").set("spark.ui.port", "22112")
    val sc = new SparkContext(sparkConf)
    val rdd: RDD[Int] = sc.makeRDD(List(1, 2, 3, 4))
    //    rdd.foreach(num => {
    //      val user = new User()
    //      println("age = " + (user.age + num))
    //    })
    // 如果算子中使用了算子外的对象，那么在执行时，需要保证这个对象能序列化
    val user = new User()
    rdd.foreach(num => {
      println("age = " + (user.age + num))
    })
    sc.stop()
  }
}

class User() extends Serializable {
  val age = 10
}