package com.zkytech.top.producer;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

public class MyProducer {
    public static void main(String[] args) throws InterruptedException {
        // 1.创建Kafka生产者的配置信息
        Properties properties = new Properties();
        // 2.指定连接的kafka集群
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,"zk0:9092");
        // 3.ACK应答级别
        properties.put("acks","all");
        // 4.重试次数
        properties.put("retries",3);
        // 5.批次大小
        properties.put("batch.size",16384);
        // 6.等待时间
        properties.put("linger.ms",1);
        // 7.RecordAccumulator缓冲区大小
        properties.put("buffer.memory",33554432);
        // 8.Key,Value的序列化类
        properties.put("key.serializer","org.apache.kafka.common.serialization.StringSerializer");
        properties.put("value.serializer","org.apache.kafka.common.serialization.StringSerializer");
        // 9.创建生产者对象
        KafkaProducer<String ,String >  producer= new KafkaProducer<String,String>(properties);

        // 10.发送数据
        for (int i = 0;i<10;i++){
            // 异步发送，如果要实现同步发送（阻塞） 可以调用producer.send().get()方法
            producer.send(new ProducerRecord<String, String>("first","zky_messagek"+i));
            System.out.println("send message");
        }
        // 11.关闭资源
        producer.close();
//        Thread.sleep(100);
        System.out.println("closed");
    }
}
