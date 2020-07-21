package com.zkytech.scala

class Animal(name:String){
  def canFly() = {
    println(s"$name can fly")
  }
}

class Rabbit(xname:String){
  val name = xname
}


object Lesson_ImplicitTrans2 {
  /**
   * 隐式转换函数
   * @param r
   * @return
   */
  implicit def RabbitToAnimal(r:Rabbit):Animal = {
    new Animal(r.name)
  }

  def main(args: Array[String]): Unit = {
    val rabbit = new Rabbit("rabbit")
    rabbit.canFly()

  }
}
