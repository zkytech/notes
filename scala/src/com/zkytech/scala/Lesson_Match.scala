package com.zkytech.scala

object Lesson_Match {
  def main(args: Array[String]): Unit = {
    val tp = (1,1.0,"abc",'a',true)
    val iter = tp.productIterator
    iter.foreach(MatchTest)


  }


  def MatchTest(o:Any) = {
    /**
     * match 模式匹配
     *  1.case _ 什么都匹配不上，放在最后
     *  2.match可以匹配值还可以匹配类型
     *  3.匹配过程中会有数值的转换
     *  4.从上往下匹配，匹配上之后会自动终止
     *  5.模式匹配外部的括号可以省略
     */
    o match{
      case 1=> println("value is 1")
      case i:Int => println(s"type is Int, value is $i")
      case s:String => println(s"type is String, value is $s")
      case 'c' => println("value is c")
      case d:Double => println(s"type is Double, value is $d")
      case b:Boolean => println(s"type is Boolean, value is $b")
      case _ => println("no match...")
    }
  }
}
