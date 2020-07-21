package com.zkytech.scala

import scala.collection.mutable.ArrayBuffer

object Lesson_Array {
  def main(args: Array[String]): Unit = {
//    val arr = Array[String]("a","b","c","d")
//    val arr2 = Array[String]("a","b","c","d")
//    val arr3= Array[String]("a","b","c","d")
//    val arrays = Array.concat(arr,arr2,arr3)
//
//    val arr5 = Array.fill(5)("hello")
//    arr5.foreach(println)
//    arrays.foreach(print)
//    print("\n")
//    val arr1 = new Array[Int](3)
//    val array = new Array[Array[Int]](3)
//
//    array(0) = Array[Int](1,2,3)
//    array(1) = Array[Int](4,5,6)
//    array(2) = Array[Int](7,8,9)
//
//    for (arr <- array ; i <- arr){
//      print(i + "\t")
//      if (i %3 == 0) print("\n")
//
//    }
//    arr1(1)= 56
//    arr1.foreach(println)
//    arr.foreach(println)
    val arr = ArrayBuffer[Int](1,2,3,4,5,6,7)
    arr.+=(6)
    arr.append(666,777,888)
    arr.+=:(100)
    arr.foreach(println)
  }
}
