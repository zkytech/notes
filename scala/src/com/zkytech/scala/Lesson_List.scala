package com.zkytech.scala

import scala.collection.mutable.ListBuffer

object Lesson_List {
  def main(args: Array[String]): Unit = {
    val list1 = ListBuffer[Int](1,2,3)
    list1.appendAll(Seq(3,4,5))
    list1.append(1,2,3)
//    val list = List(1,2,3,"a",true)
    val list= List[String]("hello scala","hello java", "hello spark")
//    val result = list.+("aaa")
//    println(result)
//    val result = list.map(s=>{
//      s.split(" ")
//    })
//    result.foreach(_.foreach(println))
//  val result = list.flatMap(s => s.split(" "))
//    result.foreach(println)
    val result = list.filter(s=>s.equals("hello scala"))
    println(list.toString())
    println(result.toString())
    println(list.count(s => s.contains("j")))
  }
}
