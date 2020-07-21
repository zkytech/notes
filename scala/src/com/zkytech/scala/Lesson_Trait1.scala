package com.zkytech.scala

trait Read{
  def read(name:String): Unit ={
    println(s"$name is reading...")
  }
}

trait Listen{
  def listen(name:String): Unit ={
    println(s"$name is listening...")
  }
}

/**
 * 一个类继承多个trait时，第一个关键字用extends，之后使用with
 */
class Human extends Read with Listen {

}

object Lesson_Trait1 {
  def main(args: Array[String]): Unit = {
    val human = new Human()
    human.read("zky")
  }
}
