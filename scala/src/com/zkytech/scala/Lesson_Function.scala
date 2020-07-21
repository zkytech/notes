package com.zkytech.scala

import java.util.Date

object Lesson_Function {
  def main(args: Array[String]): Unit = {
    /**
     * 1.方法定义
     * 1).方法体中最后返回值可以使用return，如果使用了return，那么方法体的返回值类型一定要指定
     * 2).如果方法体中没有return，默认将方法体中最后一行计算的结果当作返回值返回。 方法体的返回值可以省略，会自动推断
     * 3).定义方法传入的参数一定要指定类型
     * 4).方法的方法体如果可以一行搞定，那么方法体的“{...}"可以省略
     * 5).如果定义方法时，省略了方法名称和方法体之间的"="，那么无论方法体最后一行计算的结果是什么，都会被丢弃，返回Unit
     * 6).def定义方法
     *
     * @param a
     * @param b
     * @return
     */
    // 以下四种写法是等价的
    //    def max(a:Int,b:Int):Int={
    //      if(a>b){
    //        return a
    //      }else{
    //        return b
    //      }
    //    }

    //    def max(a:Int,b:Int)={
    //      if(a>b){
    //        a
    //      }else{
    //        b
    //      }
    //    }

    //    def max(a:Int,b:Int)= {
    //      if(a>b) a else b
    //    }

    def max(a: Int, b: Int) = if (a > b) a else b

    val result = max(100, 30)
    println(s"max is $result")

    /**
     * 2.递归方法
     * 递归方法要显式地声明方法的返回值类型
     */
    def fun(num: Int): Int = {
      if (num == 1) {
        1
      } else {
        num * fun(num - 1)
      }
    }

    println(fun(5))

    /**
     * 3.参数有默认值的方法
     */
    def fun1(a: Int = 10, b: Int = 20) = {
      a + b
    }

    println(fun1())
    println(fun1(5))
    println(fun1(b = 200))

    /**
     * 4.可变长参数的方法
     */
    def fun2(s: String*) = {
      //      println(s)
      //      for(elem <- s){
      //        println(elem)
      //      }
      //      s.foreach(f=>{
      //        println(f)
      //      })
      //      s.foreach(f => println(f))
      // f的值如果只需要用一次，可以直接简写为以下形式
      //      s.foreach(println(_))
      // 还可以进一步简写为以下形式
      s.foreach(println)
    }

    fun2("hello", "a", "b", "c")

    /**
     * 5.匿名函数
     * "=>"就是匿名函数。方法的参数是函数时，常用匿名函数
     */
    def fun5 = (a: Int, b: Int) => {
      a + b
    }

    println(fun5(100, 200))

    /**
     * 6.嵌套方法
     */
    def fun6(num: Int) = {
      def fun1(a: Int): Int = {
        if (a == 1) 1 else a * fun1(a - 1)
      }

      fun1(num)
    }

    println(fun6(5))

    /**
     * 7.偏应用函数
     * 某些情况下，方法中的参数非常多，调用这个方法非常频繁，每次调用只有固定的某个参数变化，其它都不变，可以定义偏应用函数来实现简化调用
     */
    def showLog(date: Date, log: String) = {
      println(s"date is $date, log is $log")
    }

    val date = new Date()
    showLog(date, "a")
    showLog(date, "b")
    showLog(date, "c")

    def fun7 = showLog(date, _: String)

    fun7("aaa")
    fun7("bbb")
    fun7("ccc")

    /**
     * 8.高阶函数
     * 1).方法的参数是函数
     * 2).方法的返回是函数 <要显式地写出方法的返回值类型，加 _ 就可以不显式地声明方法的返回值类型>
     * 3).方法的参数和返回都是函数
     */
    // 方法的参数是函数
    def fun8(a: Int, b: Int) = {
      a + b
    }

    def fun9(f: (Int, Int) => Int, s: String) = {
      val i = f(100, 200)
      i + "#" + s
    }

    println(fun9(fun8, "ddd"))

    // 方法的返回是函数
    //    def fun10(s:String):(String,String) => String = {
    //      def fun1(s1:String,s2:String) = {
    //          s1 + "~" + s2 + "#" +s
    //      }
    //      fun1
    //    }
    // 上面的函数还可以省略返回值的类型声明得到如下形式
    def fun10(s: String) = {
      def fun1(s1: String, s2: String) = {
        s1 + "~" + s2 + "#" + s
      }
      // "_" 表示直接将fun1作为函数的返回值
      fun1 _
    }

    println(fun10("a")("b", "c"))

    // 方法的参数和返回都是函数
    def fun12(f: (Int, Int) => Int): (String, String) => String = {
      val i: Int = f(1, 2)

      def fun1(s1: String, s2: String): String = {
        s1 + "@" + s2 + "*" + i
      }

      fun1
    }

    println(fun12((a, b) => a + b)("hello", "world"))

    /**
     * 9.柯里化函数
     */
    def fun13(a: Int, b: Int)(c: Int, d: Int) = {
      a + b + c + d
    }
    println(fun13(1,2)(3,4))
  }
}
