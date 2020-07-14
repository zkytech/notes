import org.apache.zookeeper.*;

import java.io.IOException;

public class DistributeServer {
    public static void main(String[] args) throws IOException, KeeperException, InterruptedException {
        DistributeServer server = new DistributeServer();
        // 1. 连接zooKeeper集群
        server.getConnect();
        // 2. 注册节点
        server.regist(args[0]);
        // 3. 业务逻辑处理
        server.business();
    }

    /**
     * zookeeper集群的ip:port
     */
    private String connectString = "zk0:2181,zk1:2181,zk2:2181";
    /**
     * 最大连接时间，单位毫秒
     */
    private int sessionTimeout =2000;

    private ZooKeeper zkClient;
    private void getConnect() throws IOException {

        zkClient = new ZooKeeper(connectString, sessionTimeout, new Watcher() {
            public void process(WatchedEvent watchedEvent) {

            }
        });
    }

    /**
     * 业务逻辑
     * @throws InterruptedException
     */
    private void business() throws InterruptedException {
        Thread.sleep(Long.MAX_VALUE);
    }

    /**
     * 注册节点
     * @param hostname 服务器节点名称
     * @throws KeeperException
     * @throws InterruptedException
     */
    private void regist(String hostname) throws KeeperException, InterruptedException {
        zkClient.create("/servers/server",hostname.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
        System.out.println(hostname + " is online");
    }
}
