package com.zkytech.scala

case class Person1(name:String,age:Int){

}

object Lesson_CaseClass {
  val p1 = new Person1("zhangsan",18)
  val p2 = new Person1("zhangsan",18)
  println(p1.equals(p2)) //默认实现了equals方法，以及属性的toString方法
}
