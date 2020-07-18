package com.zkytech.top.consumer;

import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;

import java.util.Arrays;
import java.util.Map;
import java.util.Properties;


/**
 * 异步提交offset的consumer
 */
public class AsyncConsumer {
    public static void main(String[] args) {
        // 1.创建消费者配置信息
        Properties properties = new Properties();

        // 2.给配置信息赋值
        // 连接的集群
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,"zk0:9092");
        // 开启自动提交offset
//        properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG,true);
        // 如果关闭自动提交offset，但又没有进行手动提交，在消费者重启后，会出现重复消费
        properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG,false);
        // 自动提交的延迟
        properties.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG,"1000");
        // key,value的反序列化
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,"org.apache.kafka.common.serialization.StringDeserializer");
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,"org.apache.kafka.common.serialization.StringDeserializer");
        // 消费者组
        properties.put(ConsumerConfig.GROUP_ID_CONFIG,"bigdata1");
        // 自动重置消费者的offset
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,"earliest");
        // 3.创建消费者
        KafkaConsumer<String,String> consumer =  new KafkaConsumer<String, String>(properties);

        // 4.订阅主题
        consumer.subscribe(Arrays.asList("first","second"));

        // 5.获取数据
        while(true){
            ConsumerRecords<String,String> consumerRecords = consumer.poll(100);
            // 6.解析并打印consumerRecords
            for(ConsumerRecord<String,String> consumerRecord:consumerRecords){
                System.out.printf("offset = %d, key = %s, value = %s%n",
                        consumerRecord.offset(),
                        consumerRecord.key(),
                        consumerRecord.value());
            }
            // 异步提交
            consumer.commitAsync(new OffsetCommitCallback() {
                public void onComplete(Map<TopicPartition, OffsetAndMetadata> offsets, Exception exception) {
                    if(exception != null){
                        System.err.println("Commit failed for " + offsets);
                    }
                }
            });
        }
//        // 7.关闭连接
//        consumer.close();
    }
}
