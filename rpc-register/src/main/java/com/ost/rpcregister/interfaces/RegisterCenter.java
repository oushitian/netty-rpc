package com.ost.rpcregister.interfaces;

/**
 * @Author xyl
 * @Create 2018-12-19 8:58
 * @Desc 服务注册接口
 **/
public interface RegisterCenter {

    /**
     * 注册接口，传入服务名和服务地址
     * @param serviceName
     * @param serviceAddress
     */
    void register(String serviceName,String serviceAddress);
}
