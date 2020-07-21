package com.zkytech.scala
object Lesson_Set {
  def main(args: Array[String]): Unit = {
//    val set = Set[Int](1,2,3,4)
//    val set1 = Set[Int](3,4,5,6)
//    val result  = set.diff(set1)
//    val result = set & set1 //取交集
//    val result = set &~ set1 //取差集
//    val result = set.intersect(set1)
    /**
     * 可变长的set
     */
//    import scala.collection.immutable.Set // 导入不可变的Set对象

    import scala.collection.mutable.Set // 导入可变的Set对象
    val set = Set[Int](1,2,3,5)
    set.+=(100)
    set.foreach(println)
  }
}
