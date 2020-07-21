package com.zkytech.scala

/**
 * trait中可以有方法体的实现或者方法体的不实现，类继承了Trait要实现Trait中没有实现的方法
 */
trait IsEquale{
  def isEq(o:Any):Boolean

  def isNotEq(o:Any) = !isEq(o)
}

class Point(xx:Int,xy:Int) extends  IsEquale {
  val x = xx
  val y = xy

  override def isEq(o: Any): Boolean = {
    o.isInstanceOf[Point] && o.asInstanceOf[Point].x == this.x
  }
}

object Lesson_Trait2 {
  def main(args: Array[String]): Unit = {
    val p1 = new Point(1,2)
    val p2 = new Point(1,3)
    println(p1.isEq(p2))
  }
}
