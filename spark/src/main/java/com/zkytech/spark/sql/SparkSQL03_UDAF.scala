package com.zkytech.spark.sql

import org.apache.spark.SparkConf
import org.apache.spark.sql.{Row, SparkSession}
import org.apache.spark.sql.expressions.{MutableAggregationBuffer, UserDefinedAggregateFunction}
import org.apache.spark.sql.types.{DataType, FloatType, IntegerType, LongType, StructField, StructType}

object SparkSQL03_UDAF {
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
    df.createOrReplaceTempView("user")

    // TODO 定义用户的自定义聚合函数
    // TODO 创建UDAF函数
    val udaf = new MyAngAgeUDAF
    // TODO 注册到sparkSQL中
    spark.udf.register("avgAge",udaf)
    // TODO 在SQL中使用聚合函数
    spark.sql("select avgAge(age) from user").show
    spark.stop()

  }
  case class User(id: Int, name: String, age: Int)

  // 自定义聚合函数
  // 1. 继承UserDefinedAggregateFunction
  // 2. 重写方法
  // 3.

  // totalAge, count
  class MyAngAgeUDAF extends  UserDefinedAggregateFunction{
    // TODO 输入数据的结构信息 ：年龄信息
    override def inputSchema: StructType = {
      val ageField: StructField = StructField("age", IntegerType)
      StructType(Array(ageField))
    }
    // TODO 缓冲区的数据结构信息 ：年龄的总和，人的数量
    override def bufferSchema: StructType = {
      val totalAgeField: StructField = StructField("totalAge", IntegerType)
      val countField: StructField = StructField("count", IntegerType)
      StructType(Array(
        totalAgeField,
        countField
      ))
    }
    // TODO 聚合函数返回的结果类型
    override def dataType: DataType = FloatType

    // TODO 函数稳定性(相同的值传进来，得到的结果是否相同)
    override def deterministic: Boolean = true

    // TODO 函数缓冲区(bufferSchema)的初始化
    override def initialize(buffer: MutableAggregationBuffer): Unit = {
      buffer(0) = 0 // totalAge
      buffer(1) = 0 // count
    }

    // TODO 更新缓冲区数据，
    override def update(buffer: MutableAggregationBuffer, input: Row): Unit = {
      // buffer对应着bufferSchema input对应着inputSchema
      buffer(0) = buffer.getInt(0) + input.getInt(0)
      buffer(1) = buffer.getInt(1) + 1
    }

    // TODO 合并缓冲区
    override def merge(buffer1: MutableAggregationBuffer, buffer2: Row): Unit = {
      buffer1(0) = buffer1.getInt(0) + buffer2.getInt(0)
      buffer1(1) = buffer1.getInt(1) + buffer2.getInt(1)

    }

    // TODO 函数的计算
    override def evaluate(buffer: Row): Float = {
      buffer.getInt(0).toFloat/buffer.getInt(1).toFloat
    }
  }
}
