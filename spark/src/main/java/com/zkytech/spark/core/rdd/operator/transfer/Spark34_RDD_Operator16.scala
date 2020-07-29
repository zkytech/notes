package com.zkytech.spark.core.rdd.operator.transfer

import org.apache.spark.rdd.RDD
import org.apache.spark.{HashPartitioner, SparkConf, SparkContext}

object Spark34_RDD_Operator16 {
  def main(args: Array[String]): Unit = {

    val sparkConf = new SparkConf().setMaster("local[*]").setAppName("File - RDD").set("spark.ui.port","22112")
    val sc = new SparkContext(sparkConf)

    val rdd1 = sc.makeRDD(List(("a",1),("b",2),("c",2)),2)
    // TODO Spark中很多的方法是基于key进行操作的，所以数据格式应该为键值对（对偶元组）
    // 如果数据类型为K-V类型，那么Spark会给RDD自动补充很多新的功能（扩展）
    // 隐式转换
    // partitionBy方法来自于PairRDDFunctions类
    // RDD的伴生对象中提供了隐式函数可以将RDD[K,V]转换为PairRDDFunctions类
    // TODO partitionBy: 根据指定的规则对数据进行分区
    //      groupBy
    //      filter => coalesce
    //      repartition => shuffle
    // partitionBy参数为分区器对象
    // HashPartitioner分区规则是将当前数据的key进行取余操作
    // HashPartitioner是Spark默认的分区器
    val rdd2: RDD[(String, Int)] = rdd1.partitionBy(new HashPartitioner(2))
//    rdd2.saveAsTextFile("output")
    // sortBy使用了RangePartitioner
    // rdd1.sortBy()

    sc.stop()

  }
}
