package com.ost.rpcregister.loadbalance;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @Author xyl
 * @Create 2018-12-20 9:08
 * @Desc 写点注释吧
 **/
public class RandomLoadBalance extends AbstractLoadBalance{

    @Override
    public String doSelect(List<String> addressLocal) {
        String serverAddress = addressLocal.get(ThreadLocalRandom.current().nextInt(addressLocal.size()));
        System.err.println("当前服务器地址:"+serverAddress);
        return serverAddress;
    }

    public static void main(String[] args) {
        for (int i = 0; i<10; i ++) {
            System.out.println(ThreadLocalRandom.current().nextInt(2));
        }
    }
}
