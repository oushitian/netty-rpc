package com.ost.nettyrpc.service;

import com.ost.nettyrpc.config.MyProperties;
import com.ost.nettyrpc.server.RpcServer;
import com.ost.nettyrpc.server.anno.RpcServerAnno;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author xyl
 * @Create 2018-11-29 9:27
 * @Desc spring启动完成后的初始化操作
 **/
@Component
@EnableConfigurationProperties(MyProperties.class)
public class ServiceInitializer implements ApplicationContextAware {

    @Autowired
    private MyProperties myProperties;

    @Autowired
    RpcServer rpcServer;

    private static ApplicationContext applicationContext;

    //所有提供的服务,拿到的是加了注解的bean
    private static Map<String, Object> serviceMap = new HashMap<>();

    public static Object getService(String name) {
        return serviceMap.get(name);
    }

    public static <T> T getBean(Class<T> clazz) {
        return applicationContext.getBean(clazz);
    }

    //spring初始化完回调
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
        Map<String,Object> beans = applicationContext.getBeansWithAnnotation(RpcServerAnno.class);
        for (Map.Entry<String,Object> entry : beans.entrySet()) {
            String className = entry.getValue().getClass().getAnnotation(RpcServerAnno.class).value().getName();  //得到注解的value的名称
            serviceMap.put(className,entry.getValue());
        }
        if (!serviceMap.isEmpty()){ //不为空说明已经初始化完，就启动服务器
            rpcServer.start0(myProperties.getZkAddress());
        }
    }

}
