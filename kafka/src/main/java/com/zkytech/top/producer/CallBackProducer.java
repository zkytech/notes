package com.zkytech.top.producer;

import org.apache.kafka.clients.producer.*;

import java.util.ArrayList;
import java.util.Properties;

public class CallBackProducer {
    public static void main(String[] args) throws InterruptedException {
        // 1.创建配置信息
        Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,"zk0:9092,zk1:9092,zk2:9092");
        // 添加k，v序列化类
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,"org.apache.kafka.common.serialization.StringSerializer");
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,"org.apache.kafka.common.serialization.StringSerializer");
        // 自定义分区分配器
        properties.put(ProducerConfig.PARTITIONER_CLASS_CONFIG,"com.zkytech.top.partitioner.MyPartitioner");
        // 添加拦截器
        ArrayList<String> interceptors = new ArrayList<String>();
        interceptors.add("com.zkytech.top.interceptor.MyInterceptor");
        properties.put(ProducerConfig.INTERCEPTOR_CLASSES_CONFIG,"com.zkytech.top.interceptor.MyInterceptor");
        // 2.创建生产者对象
        KafkaProducer<String ,String> producer = new KafkaProducer<String,String>(properties);
        // 3.发送数据
        for(int i = 0; i< 30; i++){

//            if ( i % 10 == 0){
//                System.out.println("start sleep");
//                Thread.sleep(50);
//                System.out.println("end sleep");
//            }
            producer.send(new ProducerRecord<String, String>("first",""+i, "zky-" + i), new Callback() {

                public void onCompletion(RecordMetadata recordMetadata, Exception e) {
                    if (e == null){
                        System.out.println(recordMetadata.partition() + "--" + recordMetadata.offset());
                    }else{
                        e.printStackTrace();
                    }
                }
            });
        }
        // 4.关闭资源
        producer.close();
    }
}
