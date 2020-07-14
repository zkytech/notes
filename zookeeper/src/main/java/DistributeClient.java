import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DistributeClient {
    public static void main(String[] args) {
        DistributeClient client = new DistributeClient();
        // 1. 获取zookeeper集群连接
        client.getConnect();
        // 2. 注册监听
        client.getChildren();
        // 3. 业务逻辑处理
        client.business();


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
    private void getConnect(){
        try {
            zkClient = new ZooKeeper(connectString, sessionTimeout, new Watcher() {
                public void process(WatchedEvent watchedEvent) {
                    System.out.println(watchedEvent.toString());
                    getChildren();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void business(){
        try {
            Thread.sleep(Long.MAX_VALUE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    private void getChildren(){
        try {
            // 获取/servers的子节点，并监听/servers
            List<String> children = zkClient.getChildren("/servers",true);
            ArrayList<String> hosts = new ArrayList<String>();
            for(String child : children){
                byte[] data = zkClient.getData("/servers/"+child,false,null);
                hosts.add(new String(data));
            }
            // 将在线的主机名打印到控制台
            System.out.println(hosts);
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
