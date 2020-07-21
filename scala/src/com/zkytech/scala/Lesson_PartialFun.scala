package com.zkytech.scala

/**
 * 偏函数，只能匹配一个值，匹配上了返回某个值
 * PartialFunction[A,B]， A时匹配的类型，B时匹配上返回的类型
 */
object Lesson_PartialFun {
  def MyTest:PartialFunction[String,Int] = {
    case "abc" => 2
    case "a" => 1
    case _ => 200
  }

  def main(args: Array[String]): Unit = {
  val result = MyTest("abc")
    println(result)
  }

}
