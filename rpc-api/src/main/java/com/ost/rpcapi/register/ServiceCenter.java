package com.ost.rpcapi.register;

import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.*;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * @Author xyl
 * @Create 2018-11-29 14:19
 * @Desc 注册中心，zk实现
 **/
@Slf4j
@Component
public class ServiceCenter {

    private static final String APPS_PATH = "/apps";
    private static ZooKeeper zk;

    private static void createConnect() throws IOException, InterruptedException, KeeperException {
        if (zk!=null) {
            return;
        }
        CountDownLatch countDownLatch = new CountDownLatch(1);
        String serverCenterAddress = "localhost:2181";

        zk = new ZooKeeper(serverCenterAddress, 30000, event -> {
            if (event.getState() == Watcher.Event.KeeperState.SyncConnected) {
                countDownLatch.countDown();
            }
        });
        countDownLatch.await();

        if (zk.exists(APPS_PATH, false) == null) {  //不存在根节点就创建
            zk.create(APPS_PATH, null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);     //持久化节点
        }
    }

    public static void register(String serviceName, String address){
        //服务注册
        try {
            createConnect();
            if (zk.exists(APPS_PATH + "/" + serviceName,false) == null) {       //不存在服务节点就创建
                zk.create(APPS_PATH + "/" + serviceName, null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            }
            String path = zk.create(APPS_PATH + "/" + serviceName + "/", address.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);     //临时节点
            log.info("create zookeeper node ({} => {})", path, address.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (KeeperException e) {
            e.printStackTrace();
        }
    }

    public String getService(String serviceName) throws KeeperException, InterruptedException, IOException {
        createConnect();
        List<String> apps = zk.getChildren(APPS_PATH + "/"+serviceName,false);
        if (apps.isEmpty()) {
            return null;
        }
        byte[] data = zk.getData(APPS_PATH + "/"+serviceName+"/"+apps.get(0), false, null);
        return new String(data);
    }

}
