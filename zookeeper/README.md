# zooKeeper

> [视频教程](https://www.bilibili.com/video/BV1Gb411T7sh)

## 基础

### 配置文件

`zooKeeper/conf/zoo.cnf`
```
     # The number of milliseconds of each tick 心跳间隔时间 单位ms
     tickTime=2000
     # The number of ticks that the initial
     # synchronization phase can take 启动初始化同步最多经过多少次心跳
     initLimit=10
     # The number of ticks that can pass between
     # sending a request and getting an acknowledgement 启动后 通信同步最大允许经过多少次心跳
     syncLimit=5
     # the directory where the snapshot is stored.
     # do not use /tmp for storage, /tmp here is just
     # example sakes. 数据文件目录 + 数据持久化路径
     # 这里将dataDir设置为zookeeper目录下的zkData文件夹，需要手动创建该文件夹
     dataDir=/opt/module/zookeeper/zkData
     # the port at which the clients will connect 客户端连接端口
     clientPort=2181
     # the maximum number of client connections.
     # increase this if you need to handle more clients
     #maxClientCnxns=60
     #
     # Be sure to read the maintenance section of the
     # administrator guide before turning on autopurge.
     #
     # http://zookeeper.apache.org/doc/current/zookeeperAdmin.html#sc_maintenance
     #
     # The number of snapshots to retain in dataDir
     #autopurge.snapRetainCount=3
     # Purge task interval in hours
     # Set to "0" to disable auto purge feature
     #autopurge.purgeInterval=1

     ## Metrics Providers
     #
     # https://prometheus.io Metrics Exporter
     #metricsProvider.className=org.apache.zookeeper.metrics.prometheus.PrometheusMetricsProvider
     #metricsProvider.httpPort=7000
     #metricsProvider.exportJvmInfo=true
```

### 启动 & 其它脚本

启动服务器：
```
> bin/zkServer.sh start

# 输出示例
/usr/bin/java
ZooKeeper JMX enabled by default
Using config: /opt/module/zookeeper/bin/../conf/zoo.cfg
Starting zookeeper ... STARTED
```

停止服务器
```
> bin/zkServer.sh stop

# 输出示例
/usr/bin/java
ZooKeeper JMX enabled by default
Using config: /opt/module/zookeeper/bin/../conf/zoo.cfg
Stopping zookeeper ... STOPPED
```

查看服务器状态
```
> bin/zkServer.sh status

# 输出示例
/usr/bin/java
ZooKeeper JMX enabled by default
Using config: /opt/module/zookeeper/bin/../conf/zoo.cfg
Client port found: 2181. Client address: localhost.
Mode: standalone
```

启动客户端:
```
> bin/zkCli.sh
# 启动后进入zookeeper交互界面
# > quit 退出zookeeper交互界面
```

## 内部原理

### 选举机制

1. 半数机制：只要集群中半数以上机器存活，集群就可用。所以ZooKeeper适合安装**奇数台服务器**
2. Zookeeper虽然在配置文件中并没有指定Master和Slave，但是，Zookeeper工作时，是有一个节点为Leader，其它为Follower，Leader是通过内部的选举机制**临时**产生的。
3. 选举过程：假设Zookeeper集群中的服务器依序启动，每个服务器启动时都会投一票给自己，当其发现当前票数无法决定leader时，自动将自身所获得的所有票投给下一个启动的服务器，直至某个服务器获得超过半数的票。

### 节点类型
#### 持久（President）

客户端和服务器断开连接后，创建的节点不删除

细分为以下两类：

1. 持久化目录节点：客户端与Zookeeper断开连接后，该节点依旧存在
2. 持久化顺序编号目录节点：客户端与Zookeeper断开连接后，该节点一九存在，只是Zookeeper给该节点名称进行顺序编号
> 说明：创建znode时设置顺序标识，znode名称后会附加一个值，顺序号是一个单调递增的计数器，由父节点维护
> 注意：在分布式系统中，顺序号可以被用于为所有的事件进行全局排序，这样客户端可以通过顺序号推断事件的顺序（在这里就是判断znode的上线顺序）
#### 短暂（Ephemeral）

客户端和服务器断开连接后，创建的节点自己删除

细分为以下两类：

1. 临时目录节点：客户端与Zookeeper断开连接后，该节点被删除
2. 临时顺序编号目录节点：客户端与Zookeeper断开连接后，该节点被删除，只是Zookeeper给该节点名称进行顺序编号

### stat结构体

- `cxzid` 创建节点的事务
    
    每次修改ZooKeeper状态都会收到一个cxzid形式的时间戳，也就是ZooKeeper事务ID
    
    事务ID是ZooKeeper中所有修改总的次序，每个修改都有唯一的zxid，如果zxid1小于zxid2，那么zxid1在zxid2之前发生

- `ctime` znode被创建的毫秒数（从1970年开始）
- `mzxid` znode最后更新的事务zxid
- `mtime` znode最后修改的毫秒数（从1970年开始）
- `pZxid` znode最后更新的子节点zxid
- `cversion` znode子节点变化号，znode子节点修改次数
- `dataversion` znode数据变化号
- `aclVersion` znode访问控制列表的变化号
- `ephemeralOwner` 如果是临时节点，这个是znode拥有者的session id，如果不是临时节点，则是0
- **`dataLength`** znode的数据长度
- **`numChildren`** znode子节点数量

### 监听器原理

![监听原理](_v_images/20200713180949605_24609.png)
### 写数据流程
![写数据流程](_v_images/20200713181305157_10095.png)
## 实战应用

### 准备

#### 环境

1. 所有服务器均为`centos8`

2. zookeeper版本为3.6.1（注意客户端版本要与服务端版本一致）

3. 关闭服务器上的防火墙
```
# 停止防火墙运行
systemctl stop firewalld
# 关闭防火墙开机自启
systemctl disable firewalld
```

#### 运维同步脚本

在`/usr/bin`中创建文件`xsync`写入以下内容

> 脚本使用方法:首先cd到需要同步的文件所在的目录，然后运行`xsync filename`，需配合修改`/etc/hosts`文件，并配置公钥登录
```bash
#! /bin/bash
#1获取输入参数的个数，如果没有参数直接退出
pcount=$#
if((pcount==0));then
echo no args;
exit;
fi
#2 获取文件名称
p1=$1
fname=`basename $p1`
echo fname=$fname
#3 获取上级目录到绝对路径
pdir=`cd -p $(dirname $p1);pwd`
echo pdir=$pdir
#4 获取当前用户的名称
user='root'
#5 循环
for((host=0;host<3;host++));do
echo -----------zk$host-----------
#6 将文件同步到zk0、zk1、zk2
rsync -rvl $pdir/$fname $user@zk$host:$pdir/$fname
done
```
#### 关闭防火墙

测试环境下为了方便，直接关闭所有节点的防火墙

centos8关闭防火墙命令：`systemctl stop firewalld.service`

#### 日志

zookeeper运行日志存储于`zookeeper/logs`目录

### 分布式安装部署
#### 集群规划

在zk0、zk1和zk2三个节点上部署Zookeeper

#### 配置服务器编号

在zk0的`zookeeper/zkData`目录下创建文件`myid`
```
> echo 0 > Zookeeper/zkData/myid
```
同理在zk1、zk2的该文件中中分别写入`1`、`2`

打开`zookeeper/conf/zoo.cfg`

修改数据存储路径配置
```
dataDir=/opt/module/zookeeper/zkData
```

增加如下配置：
```
#########cluster#########
server.0=zk0:2888:3888
server.1=zk1:2888:3888
server.2=zk2:2888:3888
```

#### 同步配置文件
```
> xsync zoo.cfg
```

配置参数说明
```
server.A=B:C:D
```

- A 是一个数字，表示第几号服务器；
集群模式下配置一个文件`myid`，这个文件在`zookeeper/dataDir`目录下，这个文件里面有一个数据就是A的值，**zookeeper启动时读取此文件**，拿到里面的数据与`zoo.cfg`里面的配置信息比较从而判断到底时哪个server

- B是这个服务器的ip地址
- C是这个服务器与集群中的Leader服务器交换信息的端口
- D是万一集群中的Leader服务器挂了，需要一个端口来重新进行选举，选出一个新的Leader，而这个端口就是用来执行选举时服务器相互通信的端口。

#### 集群操作


（1）分别启动各节点上的Zookeeper
```
> zookeeper/bin/zkServer.sh start
```

（2）查看服务状态
```
> zookeeper/bin/zkServer.sh status
```

#### 客户端命令行操作

1. 启动客户端
```
> zookeeper/bin/zkCli.sh
```

2. 显示所有命令

```
> help

ZooKeeper -server host:port -client-configuration properties-file cmd args
	addWatch [-m mode] path # optional mode is one of [PERSISTENT, PERSISTENT_RECURSIVE] - default is PERSISTENT_RECURSIVE
	addauth scheme auth
	close 
	config [-c] [-w] [-s]
	connect host:port
	create [-s] [-e] [-c] [-t ttl] path [data] [acl]
	delete [-v version] path
	deleteall path [-b batch size]
	delquota [-n|-b] path
	get [-s] [-w] path
	getAcl [-s] path
	getAllChildrenNumber path
	getEphemerals path
	history 
	listquota path
	ls [-s] [-w] [-R] path
	printwatches on|off
	quit 
	reconfig [-s] [-v version] [[-file path] | [-members serverID=host:port1:port2;port3[,...]*]] | [-add serverId=host:port1:port2;port3[,...]]* [-remove serverId[,...]*]
	redo cmdno
	removewatches path [-c|-d|-a] [-l]
	set [-s] [-v version] path data
	setAcl [-s] [-v version] [-R] path acl
	setquota -n|-b val path
	stat [-w] path
	sync path
	version 
Command not found: Command not found help
```

3. 查看当前znode中所包含的内容
```
> ls /

[zookeeper]
```

4. 查看节点详细数据
```
> ls -s /

[zookeeper]
cZxid = 0x0
ctime = Wed Dec 31 16:00:00 PST 1969
mZxid = 0x0
mtime = Wed Dec 31 16:00:00 PST 1969
pZxid = 0x0
cversion = -1
dataVersion = 0
aclVersion = 0
ephemeralOwner = 0x0
dataLength = 0
numChildren = 1
```

5. 分别创建两个普通(持久型)节点
```
> create /sanguo
Created /sanguo
> ls /
[sanguo, zookeeper]
> create /sanguo/shuguo "liubei"
Created /sanguo/shuguo
> ls /sanguo
[shuguo]
# 查看节点数据
> get /sanguo/shuguo
liubei
```

6. 创建短暂节点

```
> create -e /sanguo/wuguo "zhouyu"
Created /sanguo/wuguo
> ls /sanguo
[shuguo, wuguo]
# 退出客户端
> quit
# 打开客户端
> zookeeper/bin/zkCli.sh
> ls /sanguo
[shuguo]
# 之前创建的短暂节点此时已经被删除了
```

7. 创建带有序号的节点
```
> create -s /sanguo/weiguo "caocao"
Created /sanguo/weiguo0000000004
> create -s /sanguo/weiguo "caocao"
Created /sanguo/weiguo0000000005
> create -s /sanguo/weiguo "caocao"
Created /sanguo/weiguo0000000006
> ls /sanguo
[shuguo, weiguo0000000004, weiguo0000000005, weiguo0000000006]
```
如果原来没有序号节点，序号从0开始依次递增。如果源节点下已有两个节点，则在排序时从2开始，以此类推。

8. 修改节点数据值
```
> set /sanguo/shuguo "adou"
> get /sanguo/shuguo
adou
```

9. 节点值变化监听
**注意：**这种方式只能触发一次监听事件，后续改动不会继续触发
```
# 在客户端0上监听节点
[client0] get -w /sanguo

# 在客户端1上修改节点的值
[client1] set /sanguo aab

# 客户端0触发监听事件
[client0]
WATCHER::

WatchedEvent state:SyncConnected type:NodeDataChanged path:/sanguo

```

10. 路径变化（节点的子节点变化监听）
```
#  在客户端0上监听路径
[client0] ls -w  /sanguo 
[shuguo, weiguo0000000004, weiguo0000000005, weiguo0000000006]

# 在客户端1上修改路径（子节点）
[client1] create -s /sanguo/wuguo
Created /sanguo/wuguo

# 客户端0触发监听事件
[client0]
WATCHER::

WatchedEvent state:SyncConnected type:NodeChildrenChanged path:/sanguo
```

11. 删除节点
```
> delete /sanguo/wuguo
```

12.  递归删除节点
```
> deleteall /sanguo
```

13.  查看节点状态
```
> stat /zookeeper
cZxid = 0x0
ctime = Wed Dec 31 16:00:00 PST 1969
mZxid = 0x0
mtime = Wed Dec 31 16:00:00 PST 1969
pZxid = 0x0
cversion = -2
dataVersion = 0
aclVersion = 0
ephemeralOwner = 0x0
dataLength = 0
numChildren = 2
```

### API应用

1. 创建一个maven工程
2. 添加pom文件
```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>org.example</groupId>
    <artifactId>zookeeper_demo</artifactId>
    <version>1.0-SNAPSHOT</version>

    <dependencies>
        <dependency>
            <groupId>junit</groupId>
            <artifactId>junit</artifactId>
            <version>RELEASE</version>
        </dependency>
        <dependency>
            <groupId>org.apache.logging.log4j</groupId>
            <artifactId>log4j-core</artifactId>
            <version>2.8.2</version>
        </dependency>
        <dependency>
            <groupId>org.apache.zookeeper</groupId>
            <artifactId>zookeeper</artifactId>
            <version>3.6.1</version>
        </dependency>

    </dependencies>
</project>
```
3. 在resources目录下创建`log4j.properties`文件并写入以下配置:
```properties
log4j.rootLogger=INFO, stdout  
log4j.appender.stdout=org.apache.log4j.ConsoleAppender  
log4j.appender.stdout.layout=org.apache.log4j.PatternLayout  
log4j.appender.stdout.layout.ConversionPattern=%d %p [%c] - %m%n  
log4j.appender.logfile=org.apache.log4j.FileAppender  
log4j.appender.logfile.File=target/spring.log  
log4j.appender.logfile.layout=org.apache.log4j.PatternLayout  
log4j.appender.logfile.layout.ConversionPattern=%d %p [%c] - %m%n  
```

#### 创建客户端

编写测试类
```java
public class main{
    /**
    * 最大连接时间，单位毫秒
    */
    private int sessionTimeout =2000;

    private ZooKeeper zkClient;
    /**
     * 连接zookeeper集群
     * @throws IOException
     */
    @test
    public void init() throws IOException {
       zkClient = new ZooKeeper(connectString, sessionTimeout, new Watcher() {
            public void process(WatchedEvent event) {
            }
        });
    }
}
```

#### 创建子节点

修改测试类的内容如下：

```java
public class main {
    /**
     * zookeeper集群的ip:port
     */
    private String connectString = "zk0:2181,zk1:2181,zk2:2181";
    /**
     * 最大连接时间，单位毫秒
     */
    private int sessionTimeout =2000;

    private ZooKeeper zkClient;
    /**
     * 连接zookeeper集群
     * @throws IOException
     */
    @Before
    public void init() throws IOException {
       zkClient = new ZooKeeper(connectString, sessionTimeout, new Watcher() {
            public void process(WatchedEvent event) {
            }
        });
    }

    /**
     * 创建数据节点
     */
    @Test
    public void create() throws KeeperException, InterruptedException, IOException {

        String path = zkClient.create("/akafk","zky".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        System.out.println((path));
    }
}
```

#### 获取子节点并监听数据的变化

为了实现监听，需进行如下修改

1. 进程不能退出，所以在单元测试中需要使用`Thread.sleep(Long.MAX_VALUE)`

2. 编写`init`方法中的watcher.process方法，比如：
```java
    @Before
    public void init() throws IOException {
       zkClient = new ZooKeeper(connectString, sessionTimeout, new Watcher() {
            public void process(WatchedEvent event) {
                System.out.println("============start==============");
                List<String> children = null;
                try {
                    children = zkClient.getChildren("/",true);
                } catch (KeeperException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                for (String child:children){
                    System.out.println(child);
                }
                System.out.println("============end==============");
            }
        });
    }
```

#### 判断子节点是否存在

```java
    /**
     * 判断节点是否存在
     */
    @Test
    public void exist() throws KeeperException, InterruptedException {
        Stat stat = zkClient.exists("/zky",false);
        System.out.println(stat==null?"not exist":"exist");
    }
```

