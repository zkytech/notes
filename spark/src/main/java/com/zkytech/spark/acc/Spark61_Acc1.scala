package com.zkytech.spark.acc

import org.apache.spark.rdd.RDD
import org.apache.spark.util.LongAccumulator
import org.apache.spark.{SparkConf, SparkContext}

object Spark61_Acc1 {
  def main(args: Array[String]): Unit = {

    val sparkConf = new SparkConf().setMaster("local[*]").setAppName("File - RDD").set("spark.ui.port", "22112")
    val sc = new SparkContext(sparkConf)
    val rdd: RDD[Int] = sc.makeRDD(List(1, 2, 3, 4))
    // TODO 使用累加器完成数据的累加
    val sum: LongAccumulator = sc.longAccumulator("sum")
    rdd.map(("a",_)).foreach{
          // TODO 使用累加器
      case (k,v) => sum.add(v)
    }
    // TODO 获取累加器的结果
    println("结果为:" + sum.value)
    sc.stop()
  }
}
