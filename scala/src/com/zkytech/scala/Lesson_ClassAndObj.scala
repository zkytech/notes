package com.zkytech.scala

/**
 * Scala:
 * 1.Scala object相当于java中的单例，object中定义的全是静态的，相当于java中的工具类 【object默认不可传参，如果要传参的话，使用apply方法】。main方法是静态的，必须写在object中才能运行
 * 2.Scala中定义变量使用var，定义常量使用val，变量可变，常量不可变。
 * 3.Scala中每行后面都会有分号自动推断机制，不用显式地写出“；”
 * 4.建议在Scala中命名使用驼峰命名法
 * 5.Scala类中可以传参，传参一定要指定类型，有了参数就有了默认构造，类中的属性默认又getter和setter方法
 * 6.类中重写构造，构造的第一行必须先调用默认的构造. def this(...){...}
 * 7.Scala中当new Class时，类中除了方法不执行【除了构造方法】，其它语句都会执行
 * 8.在同一个scala文件中，class的名称和Object名称一样时，这个类叫做这个对象的伴生类，这个对象叫做这个类的伴生对象，他们之间可以互相访问私有变量。
 */
object Lesson_ClassAndObj {
  println("############Lesson_ClassAndObj##################")
  val name = "wangwu"

  def apply(i: Int) = {
    println("Lesson_ClassAndObj.apply is running")
    println("Score is "+i)
  }

  def apply(i:String)={
    println("apply String " + i)
  }

  def apply(i:Int, s:String)={
    println("name is " + s + " score is " + s)
  }

  def main(args: Array[String]): Unit = {
    println("+++++++++if...else...++++++++++")
    /**
     * if...else...
     */
    val age = 20
    if(age <= 20){
      println("age <= 20")
    }else if (age> 20 && age <= 30){
      println("20 < age <= 30 ")
    }else{
      println("age > 30")
    }
    println("-----------if...else...-------------")

    println("++++++++++++++++for++++++++++++++++++++")
    /**
     * fpr
     */
    val r = 1 to 10 // to 包含10 即1到10, 等价于   1.to(10)
    val r1 = 1 until 10 // until不包含10 即1到9， 等价于 1.until(10)
    println(r)
    println(r1)
    for(i <- 1 to 10){
      println(i)
    }
//    for (i <- 1 until 10){
//      for (j <- 1 until 10){
//        if (i >= j){
//          print(s"$i * $j = "+i*j+"\t")
//        }
//        if(i == j){
//          print("\n")
//        }
//      }
//    }
    // 上下两种写法等价
    for(i <- 1 until 10 ; j <- 1 until 10){
      if(i>= j){
        print(s"$i * $j = "+ i*j + "\t")
      }
      if(i == j){
        println()
      }
    }

//    for(i <- 1 to 1000; if (i > 500); if(i%2 == 0)){
//      println(i)
//    }
    val result = for(i <- 1 to 100 if(i>50) if (i%2 == 0)) yield i
    println(result)
    println("----------------for------------------")

    println("+++++++++++++++while++++++++++++++++++")
    /**
     * while
     * do...while...
     */

    println("---------------while------------------")
//    var i = 0
//    while(i <100){
//      println(s"xxx $i")
//      i += 1
//    }
    var i = 0
    do{
      println(s"xxxx $i")
      i += 1
    }while(i < 100)

    println("----------------for------------------")

    /**
     * 类和对象
     */
    //    val a =100
//    val b:Int = 200
//    println(a)
//    println(b)
//    val p = new Person("zky",25,'F')
//    val p1 = new TPerson("zky1",25)
//    p1.xname = "xxx"
//    p.sayName()
//    println(p1.xname)
//    println(p.name)
//    println(p.age)
//    printf("gender:%s \n",p.gender)
//
//    // 当传参调用Object时，会默认调用其apply方法
//    Lesson_ClassAndObj(100)
//    Lesson_ClassAndObj("zky")
//    Lesson_ClassAndObj(100,"zky")
  }
}
class TPerson(var xname:String, val xage:Int){
}

class Person(xname:String,xage:Int){
  println("++++++++++++++++Person Class++++++++++++++++++")
  val name = xname
  val age = xage
  var gender :Char = 'm'

  def this(yname:String,yage:Int,ygender:Char){
    this(yname,yage)
    this.gender = ygender
  }

  def sayName()={
    println("hello world " + Lesson_ClassAndObj.name )
  }
  println("===============Person Class++++++++++++++++++++")
}
