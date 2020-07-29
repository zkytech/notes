package com.zkytech.spark.core.rdd.operator.transfer

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object Spark05_RDD_File_Par {
  def main(args: Array[String]): Unit = {
    val sparkConf = new SparkConf().setMaster("local[*]").setAppName("File - RDD").set("spark.ui.port","22112")
    val sc = new SparkContext(sparkConf)
    // TODO Spark - 从磁盘(file)中创建RDD
    // textFile 第一个参数表示读取文件的路径
    // textFile 第二个参数表示最小分区数量
    //          默认值为：math.min(defaultParallelism, 2)
    // 1. Spark 读取文件采用的是Hadoop的读取规则
    //    文件切片规则：以字节方式来切片
    //    数据读取规则：以"行"为单位来读取
    // 2. 问题
    // TODO 文件到底切成几片（分区的数量）？
    // 文件字节数（10），预计切片数量(3)
    //    10/3 => 3 ... 1 => 4
    //    所谓的最小分区数，取决于总的字节数是否能整除分区数并且剩余的字节达到一个比率
    //    实际产生的分区数量可能大于最小分区数
    // TODO 分区的数据如何存储 ？
    // 分区数据是以行为单位读取的，而不是字节
    val fileRDD: RDD[String] = sc.textFile("input/w.txt")
    fileRDD.saveAsTextFile("output")
    val fileRDD1: RDD[String] = sc.textFile("input/w.txt",1)
    fileRDD1.saveAsTextFile("output1")
    val fileRDD2: RDD[String] = sc.textFile("input/w.txt",3)
    fileRDD2.saveAsTextFile("output2")
    val fileRDD3: RDD[String] = sc.textFile("input/w.txt",4)
    fileRDD3.saveAsTextFile("output3")
    sc.stop()
  }
}
