package com.zkytech.spark.sql

import org.apache.spark.SparkConf
import org.apache.spark.sql.expressions.{Aggregator, MutableAggregationBuffer, UserDefinedAggregateFunction}
import org.apache.spark.sql.types._
import org.apache.spark.sql.{Encoder, Encoders, Row, SparkSession}

object SparkSQL04_UDAF_Class {
  def main(args: Array[String]): Unit = {
    // TODO 创建环境对象
    val sparkConf = new SparkConf().setMaster("local[*]").setAppName("File - RDD").set("spark.ui.port", "22112")
    val spark = SparkSession.builder().config(sparkConf).getOrCreate()
    // 导入隐式转换，这里的spark其实是环境对象的名称
    // 要求这个对象必须使用val声明
    import spark.implicits._


    val rdd = spark.sparkContext.makeRDD(List(
      (1,"张三",11),
      (2,"李四",21),
      (3,"王五",33),
    ))
    val df = rdd.toDF("id","name","age")
    val ds = df.as[User]
    // TODO 定义用户的自定义聚合函数
    // TODO 创建UDAF函数
    val udaf = new MyAngAgeUDAFClass
    // TODO 在SQL中使用聚合函数
    // 因为聚合函数是强类型，那么sql中没有类型的概念，所以无法使用
    // 可以采用DSL语法方法进行访问
    // 将聚合函数转换为查询的列让DataSet访问
    ds.select(udaf.toColumn).show
    spark.stop()

  }
  case class User(id: Int, name: String, age: Int)
  case class AvgBuffer(var count:Int,var totalAge:Int)
  // 自定义聚合函数 - 强类型
  // 1. 继承Aggregator,定义泛型
  //    IN
  //    BUF
  //    OUT
  class MyAngAgeUDAFClass extends  Aggregator[User, AvgBuffer,Double]{
    override def zero: AvgBuffer = {
      AvgBuffer(0,0)
    }

    override def reduce(b: AvgBuffer, a: User): AvgBuffer = {
      b.totalAge = a.age + b.totalAge
      b.count  = b.count + 1
      b
    }

    override def merge(b1: AvgBuffer, b2: AvgBuffer): AvgBuffer = {
      b1.totalAge = b1.totalAge + b2.totalAge
      b1.count = b1.count + b2.count
      b1
    }

    override def finish(reduction: AvgBuffer): Double = {
      reduction.totalAge.toDouble / reduction.count.toDouble
    }

    override def bufferEncoder: Encoder[AvgBuffer] = Encoders.product // 固定写法 统一用Encoders.product

    override def outputEncoder: Encoder[Double] = Encoders.scalaDouble // 固定写法 如果返回的是double就返回scalaDouble
  }
}
