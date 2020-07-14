import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

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

    /**
     * 创建数据节点
     */
    @Test
    public void create() throws KeeperException, InterruptedException, IOException {

        String path = zkClient.create("/akafk","zky".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        System.out.println((path));
    }

    /**
     * 获取子节点 并监控节点的变化
     */
    @Test
    public void getDataAndWatch() throws KeeperException, InterruptedException {
        List<String> children = zkClient.getChildren("/",true);
        for (String child:children){
            System.out.println(child);
        }
        Thread.sleep(Long.MAX_VALUE);
    }

    /**
     * 判断节点是否存在
     */
    @Test
    public void exist() throws KeeperException, InterruptedException {
        Stat stat = zkClient.exists("/zky",false);
        System.out.println(stat==null?"not exist":"exist");
    }
}
