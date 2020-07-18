package com.zkytech.top.partitioner;

import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;

import java.util.Map;

public class MyPartitioner implements Partitioner {


    public int partition(String s, Object o, byte[] bytes, Object o1, byte[] bytes1, Cluster cluster) {
        int numPartitions = cluster.partitionCountForTopic(s);
//        return Arrays.toString(bytes).hashCode() % numPartitions;
        return 0;
    }

    public void close() {

    }

    public void configure(Map<String, ?> map) {

    }
}
