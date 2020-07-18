package com.zkytech.top.interceptor;

import org.apache.kafka.clients.producer.ProducerInterceptor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.Map;

public class MyInterceptor implements ProducerInterceptor {
    /**
     * 发送数据前，修改value的值，为其添加一个时间戳前缀
     * @param record
     * @return
     */
    public ProducerRecord onSend(ProducerRecord record) {
        // 取出数据
        String value = (String) record.value();
        // 创建一个新的ProducerRecord对象，并返回
        return new ProducerRecord<String,String>(record.topic(),record.partition(), (String) record.key(),System.currentTimeMillis() + "," + record.value());
    }
    private int success = 0;
    private int error = 0;
    /**
     * 实现计数功能，发送成功计数和失败计数
     * @param metadata
     * @param exception
     */
    public void onAcknowledgement(RecordMetadata metadata, Exception exception) {
        if(exception==null){
            success ++;
        }else{
            error ++;
        }
    }

    public void close() {
        System.out.printf("success:%d   error:%d",success,error);
    }

    public void configure(Map<String, ?> configs) {

    }
}
