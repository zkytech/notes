package com.zkytech.spark

object test {
  def main(args: Array[String]): Unit = {
    val l1 = List[String]("hello scala","hello spark")
    l1.map(_.split(" ")).foreach(va => println(va(1)))

    println("-----------------------")
    l1.flatMap(_.split(" ")).foreach(println)
  }
}
