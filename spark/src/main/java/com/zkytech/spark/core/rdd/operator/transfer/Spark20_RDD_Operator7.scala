package com.zkytech.spark.core.rdd.operator.transfer

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object Spark20_RDD_Operator7 {
  def main(args: Array[String]): Unit = {
    // glom => 将每个分区的数据转换为数组

    val sparkConf = new SparkConf().setMaster("local[*]").setAppName("File - RDD").set("spark.ui.port","22112")
    val sc = new SparkContext(sparkConf)

    val dataRDD = sc.makeRDD(List(1,2,3,4,5,6),3)
    // TODO 分组
    // groupBy 方法可以根据指定的规则进行分组，指定的规则的返回值就是分组的key
    // groupBy 方法的返回值为元组
    //          元组中的第一个元素 表示分组的key
    //          元组中的第二个元素 表示相同key的数据形成的可迭代的集合
    // groupBy方法执行完毕之后，会将数据进行分组操作，但是分区是不会改变的，
    //        不同组的数据会打乱放在不同的分区中
    // 如果将上有的分区的数据打乱重新组合到下游的分区中，那么这个操作称之为shuffle
    // 如果数据被打乱重新组合，那么数据就可能出现不均匀的情况，可以改变下游RDD的数据分区
    // groupBy方法会导致数据不均匀，产生shuffle操作，如果想改变分区，可以传递参数
    val rdd: RDD[(Int, Iterable[Int])] = dataRDD.groupBy(_ % 2,2)
    println("分组后数据分区的数量=" + rdd.glom().collect().length)
    println(rdd.collect().mkString(","))
    sc.stop()
  }
}
