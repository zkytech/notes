package com.zkytech.spark.wordcount

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object Spark01_WordCount {
  def main(args: Array[String]): Unit = {
    // TODO Spark - WordCount
    // Spark是一个计算框架
    // 开发人员是使用Spark框架的API实现计算功能

    // TODO 1. 准备Spark环境
    // setMaster: 设置Spark环境的位置
    val sparkConf = new SparkConf().setMaster("local").setAppName("wordCount")
    // TODO 2. 建立和Spark的连接
    // jdbc: connection
    val sc = new SparkContext(sparkConf)
    // TODO 3. 实现业务操作
    // TODO 3.1 读取指定目录下的数据文件（多个）
    // 参数path可以指向单一的文件，也可以指向文件目录
    val fileRDD = sc.textFile("C:\\Users\\zhang\\Desktop\\projects\\spark\\input")

    // TODO 3.2 将读取的内容进行扁平化操作，切分单词
    val wordRDD: RDD[String] = fileRDD.flatMap(_.split(" "))

    // TODO 3.3 将分词后的数据进行结构转换
    // word => (word, 1)
    val mapRDD: RDD[(String, Int)] = wordRDD.map(wd => (wd, 1))

    // TODO 3.4 将转换结构后的数据进行分组聚合
    // reduceByKey方法的作用表示根据数据key进行分组，然后对value进行统计聚合
    val wordToSumRDD: RDD[(String, Int)] = mapRDD.reduceByKey(_ + _)

    // TODO 3.5 将聚合结果采集后打印到控制台
    val wordCountArray: Array[(String, Int)] = wordToSumRDD.collect()
    println(wordCountArray.mkString(","))

    // TODO 4. 释放连接
    sc.stop()

    //    // TODO 3.3 将分词后的数据进行分组（单词）
    //    val groupRDD: RDD[(String, Iterable[String])] = wordRDD.groupBy(word => word)
    //
    //    // TODO 3.4 将分组后的数据进行聚合：(word, count)
    //    val mapRDD: RDD[(String, Int)] = groupRDD.map {
    //      case (word, iter) => {
    //        (word, iter.size)
    //      }
    //    }
    //
    //    // TODO 3.5 将聚合的结果采集后打印到控制台
    //    val wordCountArray: Array[(String, Int)] = mapRDD.collect()
    //    println(wordCountArray.mkString(","))

    // TODO 4. 释放连接
  }
}
