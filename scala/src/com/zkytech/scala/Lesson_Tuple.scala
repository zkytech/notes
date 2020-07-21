package com.zkytech.scala

object Lesson_Tuple {
  def main(args: Array[String]): Unit = {
    val tuple1 = new Tuple1("hello")
    val tuple2 = new Tuple2("a",100)
    val tuple3 = new Tuple3(1,true,"c")

    /**
     * 遍历 Tuple
      */
    val iter = tuple3.productIterator
    iter.foreach(println)
//    while (iter.hasNext){
//      println(iter.next())
//    }
  }
}
