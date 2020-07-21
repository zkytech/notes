package com.zkytech.scala

object Lesson_ImplicitTrans {
//  def sayName(implicit name:String) = {
//    println(s"$name is a student ...")
//  }

  def sayName(age:Int)(implicit name:String) = {
    println(s"$name is a student, age = $age")
  }
  def main(args: Array[String]): Unit = {
    implicit  val name1 = "zhangsan"
//    sayName
    sayName(12)
  }
}
