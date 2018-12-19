package com.ost.rpcregister;

import com.ost.rpcregister.conf.ZKConfig;
import com.ost.rpcregister.interfaces.RegisterCenter;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

/**
 * @Author xyl
 * @Create 2018-12-19 9:02
 * @Desc 写点注释吧
 **/
public class DefaultRegisterCenter implements RegisterCenter {

    public CuratorFramework curatorFramework;

    public DefaultRegisterCenter(){
        //初始化连接
        curatorFramework = CuratorFrameworkFactory.builder().connectString(ZKConfig.CONNECTION)
                .sessionTimeoutMs(3000).retryPolicy(new ExponentialBackoffRetry(1000,
                        10)).build();
        curatorFramework.start();
    }

    @Override
    public void register(String serviceName, String serviceAddress) {
        //注册服务的路径名称
        String path = ZKConfig.REGISTER_ROOT_PATH + "/" + serviceName;

        //判断路径是否存在，不存在则创建
        if(curatorFramework == null){
            return;
        }

        try {
            if (curatorFramework.checkExists().forPath(path) == null){
                //创建持久化节点,随意加个数据
                curatorFramework.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath(path,"0".getBytes());
            }

            String realPath = path+ "/" + serviceAddress;
            //创建临时节点
            String rsNode=curatorFramework.create().withMode(CreateMode.EPHEMERAL).
                    forPath(realPath,serviceAddress.getBytes());
            System.out.println("服务注册成功："+rsNode);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {
        RegisterCenter registerCenter = new DefaultRegisterCenter();
        ((DefaultRegisterCenter) registerCenter).curatorFramework.create()
                .creatingParentContainersIfNeeded()
                .withMode(CreateMode.EPHEMERAL)
                .forPath("/path","init".getBytes());
    }

}
