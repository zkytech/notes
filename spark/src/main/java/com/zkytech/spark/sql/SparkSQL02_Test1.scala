package com.zkytech.spark.sql

import org.apache.spark.SparkConf
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{DataFrame, Dataset, Row, SparkSession}

object SparkSQL02_Test1 {
  def main(args: Array[String]): Unit = {
    // TODO 创建环境对象
    val sparkConf = new SparkConf().setMaster("local[*]").setAppName("File - RDD").set("spark.ui.port", "22112")
    val spark = SparkSession.builder().config(sparkConf).getOrCreate()
    // 导入隐式转换，这里的spark其实是环境对象的名称
    // 要求这个对象必须使用val声明
    import spark.implicits._


    val rdd = spark.sparkContext.makeRDD(List(
      (1,"张三",11),
      (2,"李四",22),
      (3,"王五",33),
    ))
    val df = rdd.toDF("id","name","age")
    df.createOrReplaceTempView("user")


//    val df: DataFrame = rdd.toDF("id", "name", "age")
//    val ds: Dataset[Row] = df.map(row => {
//      val id = row(0)
//      val name = row(1)
//      val age = row(2)
//      Row(id, "name:" + name, age)
//    })

//    val userRDD: RDD[User] = rdd.map {
//      case (id, name, age) => {
//        User(id, name, age)
//      }
//    }
//    val userDS: Dataset[User] = userRDD.toDS()
//    val newDS: Dataset[User] = userDS.map(user => {
//      User(user.id, "name:" + user.name, user.age)
//    })
//    newDS.show()
    // TODO 使用自定义函数在SQL中完成数据的转换操作
    spark.udf.register("addName",(x:String) => "name:"+x)
    spark.sql("select addName(name) from user").show
    // TODO 释放环境对象
    spark.stop()

  }
  case class User(id: Int, name: String, age: Int)

}
