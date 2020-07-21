package com.zkytech.scala

object Lesson_Map {
  def main(args: Array[String]): Unit = {
    // import scala.collection.mutable.Map // 导入可变的Map
    val map = Map[String,Int]("a"->100,"b"->200,"c" ->300,("c",400))
    println(map)
    for (elem <- map){
      println(elem)
      println(elem._1)
    }

    /**
     * 可以通过map.keys 获取所有的key
     *    通过map.values 获取所有的value
     */
    println(map.get("c"))
    println(map.get("aa"))
  }
}
