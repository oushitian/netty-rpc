package com.ost.rpcregister.loadbalance;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author xyl
 * @Create 2018-12-19 15:50
 * @Desc 写点注释吧
 **/
public class RoundRobinLoadBalance extends AbstractLoadBalance{

    private static AtomicInteger atomicInteger = new AtomicInteger(0);

    @Override
    public String doSelect(List<String> addressLocal) {
        if (atomicInteger.intValue() > addressLocal.size()) {
            atomicInteger.set(0);
        }
        String serverAddress = addressLocal.get(atomicInteger.get());
        atomicInteger.getAndIncrement();
        System.err.println("当前服务器地址:"+serverAddress);
        return serverAddress;
    }

}
