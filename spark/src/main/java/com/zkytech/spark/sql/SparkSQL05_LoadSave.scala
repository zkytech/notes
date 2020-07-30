package com.zkytech.spark.sql

import org.apache.spark.SparkConf
import org.apache.spark.sql.expressions.Aggregator
import org.apache.spark.sql.{DataFrame, Encoder, Encoders, SaveMode, SparkSession}

object SparkSQL05_LoadSave {
  def main(args: Array[String]): Unit = {
    // TODO 创建环境对象
    val sparkConf = new SparkConf().setMaster("local[*]").setAppName("File - RDD").set("spark.ui.port", "22112")
    val spark = SparkSession.builder().config(sparkConf).getOrCreate()
    // 导入隐式转换，这里的spark其实是环境对象的名称
    // 要求这个对象必须使用val声明
    import spark.implicits._
    // TODO SparkSQL通用的读取和保存
    // input/user.json is not a Parquet file. expected magic number at tail [80, 65, 82, 49] but found [58, 32, 49, 125]
    // SparkSQL通用读取的默认数据格式为Parquet列式存储格式
//    val frame: DataFrame = spark.read.load("input/users.parquet")
    // 如果想要改变读取文件的格式，需要使用特殊的操作
    // 如果读取的文件格式是json格式，spark对json文件的格式有要求
    // JSON => JavaScript Object Notation
    // Spark读取文件默认是以行为单位读取的
    // Spark读取JSON文件时，要求文件中的每一行符合JSON的格式要求
    // 如果文件格式不正确，那么不会发生错误，但解析结果不正确
    // TODO 通用读取
    val frame: DataFrame = spark.read.format("json").load("input/user.json")
    // TODO 通用保存
    // TODO sparkSQL默认通用保存的文件格式为parquet
    // 如果想要保存的格式是指定的格式，比如json，那么需要进行对应的格式化操作
    // 如果路径已经存在，那么执行保存操作会发生错误
    // 如果非要在路径已经存在的情况下保存数据，那么可以使用保存模式
    frame.write.mode(SaveMode.Append).format("json").save("output")
//    frame.write.save("output")
    frame.show()


    spark.stop()

  }
  case class User(id: Int, name: String, age: Int)


}
