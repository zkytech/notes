package com.zkytech.scala
class Rabbit1(xname:String){
  val name = xname
}



object Lesson_ImplicitTrans3 {

  implicit class Animal1(r:Rabbit1){
    def showName()={
      println(s"${r.name} is Rabbit ")
    }
  }

  def main(args: Array[String]): Unit = {
    val rabbit = new Rabbit1("rabbit")
    rabbit.showName()

  }
}
