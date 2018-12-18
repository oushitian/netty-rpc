package com.ost.rpcserver.service;

import com.ost.rpcserver.annotation.RpcServerAnno;
import com.ost.rpcserver.config.MyProperties;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author xyl
 * @Create 2018-12-18 13:55
 * @Desc 写点注释吧
 **/
@Component
@EnableConfigurationProperties(MyProperties.class)
public class ServiceBean extends ServiceConfig implements InitializingBean, ApplicationContextAware {

    @Autowired
    private MyProperties myProperties;

    private static ApplicationContext applicationContext;

    //所有提供的服务,拿到的是加了注解的bean
    private static Map<String, Object> serviceMap = new HashMap<>();

    public static Object getService(String name) {
        return serviceMap.get(name);
    }

    public static <T> T getBean(Class<T> clazz) {
        return applicationContext.getBean(clazz);
    }

    //负责初始化的serviceMap赋值
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
        Map<String,Object> beans = applicationContext.getBeansWithAnnotation(RpcServerAnno.class);
        for (Map.Entry<String,Object> entry : beans.entrySet()) {
            String className = entry.getValue().getClass().getAnnotation(RpcServerAnno.class).value().getName();  //得到注解的value的名称
            serviceMap.put(className,entry.getValue());
        }
    }

    //负责启动serviceConfig
    @Override
    public void afterPropertiesSet() throws Exception {
        if (!serviceMap.isEmpty()){ //不为空说明已经初始化完，就启动服务器
            //rpcServer.start0(myProperties.getZkAddress());
            export(null);
        }
    }
}
