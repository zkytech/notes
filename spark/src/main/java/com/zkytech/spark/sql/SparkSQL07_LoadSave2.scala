package com.zkytech.spark.sql

import org.apache.spark.SparkConf
import org.apache.spark.sql.{DataFrame, SaveMode, SparkSession}

object SparkSQL07_LoadSave2 {
  def main(args: Array[String]): Unit = {
    // TODO 创建环境对象
    val sparkConf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("File - RDD").set("spark.ui.port", "22112")
    val spark: SparkSession = SparkSession.builder().config(sparkConf).getOrCreate()
    // 直接从文件中查询数据
    spark.sql("select * from json.`input/user.json`").show


    spark.stop()

  }
  case class User(id: Int, name: String, age: Int)


}
