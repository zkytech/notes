package com.zkytech.spark.sql

import org.apache.spark.SparkConf
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{DataFrame, Dataset, Row, SparkSession}

object SparkSQL01_Test {
  def main(args: Array[String]): Unit = {
    // TODO 创建环境对象
    val sparkConf = new SparkConf().setMaster("local[*]").setAppName("File - RDD").set("spark.ui.port", "22112")
    val spark = SparkSession.builder().config(sparkConf).getOrCreate()
    // 导入隐式转换，这里的spark其实是环境对象的名称
    // 要求这个对象必须使用val声明
    import spark.implicits._
    // TODO 逻辑操作
    val jsonDF = spark.read.json("input/user.json")
    // TODO SQL

    jsonDF.createOrReplaceTempView("user")
    spark.sql("select * from user").show
    val rdd = spark.sparkContext.makeRDD(List(
      (1,"张三",11),
      (2,"李四",22),
      (3,"王五",33),
    ))
    // TODO DSL
    // 如果查询列名采用单引号
    jsonDF.select('name,'age + 5).show
    // TODO RDD <=> DataFrame
    val df: DataFrame = rdd.toDF("id", "name", "age")
    val dfToRDD: RDD[Row] = df.rdd
    // TODO RDD <=> DataSet
    val userRDD = rdd.map{
      case(id,name,age) => {
        User(id,name,age)
      }
    }
    val userDS: Dataset[User] = userRDD.toDS()
    val dsToRDD: RDD[User] = userDS.rdd
    // TODO DataFrame <=> DataSet
    val dfToDS: Dataset[User] = df.as[User]
    val dsToDF: DataFrame = dfToDS.toDF()
    rdd.foreach(println)
    df.show()
    userDS.show()
    // TODO 释放环境对象
    spark.stop()

  }
  case class User(id: Int, name: String, age: Int)

}
