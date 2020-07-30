package com.zkytech.spark.sql

import org.apache.spark.SparkConf
import org.apache.spark.sql.{DataFrame, SparkSession}

object SparkSQL08_LoadCSV {
  def main(args: Array[String]): Unit = {
    // TODO 创建环境对象
    val sparkConf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("File - RDD").set("spark.ui.port", "22112")
    val spark: SparkSession = SparkSession.builder().config(sparkConf).getOrCreate()
    // 直接从文件中查询数据
    val frame: DataFrame = spark.read.format("csv").option("sep", ";").option("inferSchema", "true").option("header", "true").load("input/people.csv")
    frame.show()
    spark.stop()

  }
  case class User(id: Int, name: String, age: Int)


}
