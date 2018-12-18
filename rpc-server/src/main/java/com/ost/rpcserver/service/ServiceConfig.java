package com.ost.rpcserver.service;


import com.ost.rpcserver.config.MyProperties;
import com.ost.rpcserver.server.RpcServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @Author xyl
 * @Create 2018-12-18 14:24
 * @Desc 写点注释吧
 **/
@Component
@EnableConfigurationProperties(MyProperties.class)
public class ServiceConfig {

    @Autowired
    private MyProperties myProperties;

    private static final ScheduledExecutorService delayExportExecutor = Executors.newSingleThreadScheduledExecutor();

    private volatile boolean exported;      //发布标记

    /**
     * 是否延迟发布
     */
    public synchronized void export(){
        //判断是否延迟加载,获取配置文件的属性
        Integer delay = myProperties.getDelay();
        if (delay != null && delay > 0) {
            delayExportExecutor.schedule(() -> doExport(), delay, TimeUnit.MILLISECONDS);
        }else {
            doExport();
        }
    }

    private void doExport() {
        if (exported){
            return;
        }
        exported = Boolean.TRUE;
        new RpcServer().start0(myProperties.getServerAddress());
    }

}
