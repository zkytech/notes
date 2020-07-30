package com.zkytech.spark.sql

import org.apache.spark.SparkConf
import org.apache.spark.sql.{DataFrame, SparkSession}

object SparkSQL09_LoadMysql {
  def main(args: Array[String]): Unit = {
    // TODO 创建环境对象
    val sparkConf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("File - RDD").set("spark.ui.port", "22112")
    val spark: SparkSession = SparkSession.builder().config(sparkConf).getOrCreate()
    // 直接从mysql中查询数据
    val frame: DataFrame = spark.read.format("jdbc")
      .option("url", "jdbc:mysql://zkytech.top:3306/fuck_sina")
      .option("driver", "com.mysql.jdbc.Driver")
      .option("password", "160811")
      .option("user", "root")
      .option("dbtable", "HOT_SEARCH")
      .load()
    // 保存数据
    frame.write.format("jdbc")
      .option("url", "jdbc:mysql://zkytech.top:3306/fuck_sina")
      .option("driver", "com.mysql.jdbc.Driver")
      .option("password", "160811")
      .option("user", "root")
      .option("dbtable", "HOT_SEARCH1")
      .save()

    spark.stop()

  }
  case class User(id: Int, name: String, age: Int)


}
