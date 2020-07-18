# Kafka

[视频教程](https://www.bilibili.com/video/BV1a4411B7V9)

> 该笔记不完整，只是记录了基础内容。

## 基础
### 配置
`config/server.properties`
```properties
# 当前服务器的id，每台机器都不一样，需要单独配置
broker.id=0
# 数据存储路径，并非“日志”，日志默认存储在log文件夹
log.dirs=/opt/module/kafka/data
# 数据最长留存时间
log.retention.hours=168
# zookeeper集群地址
zookeeper.connect=zk0:1281,zk1:1281,zk2:1281
```
### 基本操作

#### 启动

```bash
# bin/kafka-server-start.sh -daemon server配置文件路径
# -daemon 以守护进程的方式运行
bin/kafka-server-start.sh -daemon config/server.properties
```

#### 关闭
```bash
bin/kafka-server-stop.sh stop
```

#### 重置

重置kafka需完成以下三个步骤

1. 清空配置文件`config/server.properties`中的`log.dirs`
2. 清空`kafka/logs`文件夹
3. 重置zookeeper

### 命令行操作

#### 查看当前服务器中的所有topic
```bash
bin/kafka-topics.sh --zookeeper zk0:2181 --list
```

#### 创建topic
```bash
bin/kafka-topics.sh --zookeeper zk0:2181 --create --replication-factor 3 --partitions 1 --topic first
```
选项说明:
--topic 定义topic名
--replication-factor 定义副本数
--partitions 定义分区数

#### 删除topic
```bash
bin/kafka-topics.sh --zookeeper zk0L2181 --delete -topic first
```

#### 发送消息
```bash
bin/kafka console producer.sh --broker-list zk0:9092 --topic first
```

#### 消费消息
```bash
bin/kafka-console-consumer.sh --bootstrap-server zk0:9092 --topic first
bin/kafka-console-consumer.sh --bootstrap-server zk0:9092 --from-begining --topic first
```
--from-begining: 会把主题中以往所有的数据都读取出来

#### 查看某个topic的详情
```bash
bin/kafka-topics.sh --zookeeper zk0:2181 --describe --topic first
```

#### 修改分区数
```bash
bin/kafka-topics.sh --zookeeper zk0:2181 --alter --topic first --partitions 6
```
