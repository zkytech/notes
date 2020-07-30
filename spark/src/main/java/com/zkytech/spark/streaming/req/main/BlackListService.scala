package com.zkytech.spark.streaming.req.main

object BlackListService {
  def main(args: Array[String]): Unit = {
    // TODO 读取kafka的数据
    // TODO 将数据转换为样例类来使用

    // TODO 周期性获取黑名单的信息，判断当前用户的点击数据是否在黑名单中
    // TODO 如果用户在黑名单中，那么将数据过滤掉，不会进行统计
    // TODO 将正常的数据进行点击量统计

    // TODO 将统计的结果中超过阈值的用户信息拉入到黑名单中
  }
}
