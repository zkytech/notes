package com.zkytech.spark.streaming.req.main

import java.sql.{Connection, Date, PreparedStatement, ResultSet}
import java.text.SimpleDateFormat

import com.zkytech.spark.streaming.bean.Ad_Click_Log
import com.zkytech.spark.util.{JdbcUtil, MyKafkaUtil}
import org.apache.kafka.clients.consumer.{ConsumerConfig, ConsumerRecord}
import org.apache.spark.SparkConf
import org.apache.spark.rdd.RDD
import org.apache.spark.streaming.{Duration, StreamingContext}
import org.apache.spark.streaming.dstream.{DStream, InputDStream}
import org.apache.spark.streaming.kafka010.{ConsumerStrategies, KafkaUtils, LocationStrategies}

import scala.collection.mutable.ListBuffer

object BlackListService {
  def main(args: Array[String]): Unit = {
    val sparkConf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("File - RDD").set("spark.ui.port", "22112")
    val ssc = new StreamingContext(sparkConf, Duration(3000)) // 第二个参数 数据采集时间周期
    // TODO 使用SparkStreaming读取Kafka的数据
    val ds: InputDStream[ConsumerRecord[String, String]] = MyKafkaUtil.getKafkaStream("atguigu", ssc)
    // TODO 将数据转换为样例类来使用
    val logDS: DStream[Ad_Click_Log] = ds.map((data: ConsumerRecord[String, String]) => {
      val datas: Array[String] = data.value().split(" ")
      Ad_Click_Log(datas(0), datas(1), datas(2), datas(3), datas(4))
    })

    // TODO 周期性获取黑名单的信息，判断当前用户的点击数据是否在黑名单中
    val reduceDS: DStream[((String, String, String), Int)] = logDS.transform(
      rdd => {
        // TODO 读取mysql数据库，获取黑名单信息
        val connection: Connection = JdbcUtil.getConnection
        val pstat: PreparedStatement = connection.prepareStatement("select userid from black_list")
        val rs: ResultSet = pstat.executeQuery()
        val blackIds: ListBuffer[String] = ListBuffer[String]()
        while (rs.next()) {

          blackIds.append(rs.getString(1))
        }
        rs.close()
        pstat.close()
        connection.close()
         // TODO 如果用户在黑名单中，那么将数据过滤掉，不会进行统计
        val filter_rdd: RDD[Ad_Click_Log] = rdd.filter(log => {
          !blackIds.contains(log.userid)
        })
        // TODO 将正常的数据进行点击量统计
        val sdf = new SimpleDateFormat("yyyy-MM-dd")

        val mapRDD: RDD[((String, String, String), Int)] = filter_rdd.map(
          log => {
            val date = new Date(log.ts.toLong)
            ((sdf.format(date), log.userid, log.adid), 1)
          }
        )

        mapRDD.reduceByKey {
          case (a: Int, b: Int) => a + b
          case _ => {
            0
          }
        }

      }
    )
    // TODO 将统计的结果中超过阈值的用户信息拉入到黑名单中
    reduceDS.foreachRDD(
      rdd => {

        rdd.foreachPartition(

          datas => {
            val conn: Connection = JdbcUtil.getConnection
            // Data ：每一个采集周期中用户点击同一个广告的数量
            // 统计结果应该是放在mysql / redis中
            // TODO 更新(新增)用户的点击数量
            val pstat: PreparedStatement = conn.prepareStatement(
              """
                |insert into user_ad_count (dt,userid,adid,count)
                |values(?,?,?,?)
                |on duplicate key
                |update count = count + ?
                |""".stripMargin)
            // TODO 获取最新的用户统计数量
            // TODO 判断是否超过阈值
            // TODO 如果超过阈值，拉入到黑名单
            val pstat1: PreparedStatement = conn.prepareStatement(
              """
                |insert into black_list (userid)
                |select userid from user_ad_count
                |where dt=? and userid=? and adid = ? and count >= 100
                |on duplicate key
                |update userid=?
                |""".stripMargin)
            datas.foreach {
              case ((day, userid, adid), sum) => {


                pstat.setString(1, day)
                pstat.setString(2, userid)
                pstat.setString(3, adid)
                pstat.setInt(4, sum)
                pstat.setInt(5, sum)
                pstat.executeUpdate()


                pstat1.setString(1, day)
                pstat1.setString(2, userid)
                pstat1.setString(3, adid)
                pstat1.setString(4, userid)
                pstat1.executeUpdate()


              }
            }
            pstat.close()
            pstat1.close()
            conn.close()
          }
        )
      }
    )
    ssc.start()
    ssc.awaitTermination()
  }
}
