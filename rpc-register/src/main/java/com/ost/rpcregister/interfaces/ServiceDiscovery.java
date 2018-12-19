package com.ost.rpcregister.interfaces;

/**
 * @Author xyl
 * @Create 2018-12-19 11:09
 * @Desc 服务发现接口
 **/
public interface ServiceDiscovery {

    /**
     * 根据请求的服务地址，获得对应的调用地址
     * @param serviceName
     * @return
     */
    String discover(String serviceName);

}
