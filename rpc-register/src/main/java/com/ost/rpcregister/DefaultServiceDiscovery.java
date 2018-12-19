package com.ost.rpcregister;

import com.ost.rpcregister.conf.ZKConfig;
import com.ost.rpcregister.interfaces.ServiceDiscovery;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author xyl
 * @Create 2018-12-19 11:11
 * @Desc 写点注释吧
 **/
public class DefaultServiceDiscovery implements ServiceDiscovery {

    private List<String> addressLocal = new ArrayList<>();

    private CuratorFramework curatorFramework;

    /**
     * 传入zk的地址
     * @param zkAddress
     */
    public DefaultServiceDiscovery(String zkAddress){
        curatorFramework = CuratorFrameworkFactory.builder().connectString(zkAddress)
                .sessionTimeoutMs(3000).retryPolicy(new ExponentialBackoffRetry(1000,
                        10)).build();
        curatorFramework.start();
    }

    /**
     * 服务发现
     * @param serviceName
     * @return
     */
    @Override
    public String discover(String serviceName) {

        String path = ZKConfig.REGISTER_ROOT_PATH + "/" + serviceName;

        try {
            //得到所有的子节点目录
            addressLocal = curatorFramework.getChildren().forPath(path);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //监听子节点的变化
        registerWatcher(path);

        //这里可以实现负载均衡算法
        return addressLocal.get(0);
    }

    private void registerWatcher(final String path){

        PathChildrenCache childrenCache = new PathChildrenCache(curatorFramework,path,true);

        childrenCache.getListenable().addListener((curatorFramework, pathChildrenCacheEvent) -> addressLocal=curatorFramework.getChildren().forPath(path));

        try {
            childrenCache.start();
        } catch (Exception e) {
            throw new RuntimeException("注册PatchChild Watcher 异常"+e);
        }


    }

}
