package com.ost.rpcclient.client.proxy;

import com.ost.rpcapi.request.RpcRequest;
import com.ost.rpcapi.response.RpcResponse;
import com.ost.rpcclient.client.RpcClient;
import com.ost.rpcregister.DefaultServiceDiscovery;
import com.ost.rpcregister.conf.ZKConfig;
import com.ost.rpcregister.interfaces.ServiceDiscovery;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @Author xyl
 * @Create 2018-11-29 10:38
 * @Desc 客户端的代理，对调用者透明化
 **/
public class RpcProxy implements InvocationHandler {

    private RpcClient client;

    private ServiceDiscovery serviceDiscovery = new DefaultServiceDiscovery(ZKConfig.CONNECTION);

    private static AtomicLong id = new AtomicLong(0);

    public static <T> T getProxy(Class<?> clazz){
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(),new Class[]{clazz},new RpcProxy());
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //调用  封装request参数  这里的request其实就是实际类的信息，这里就是ISay的类信息
        RpcRequest rpcRequest = new RpcRequest();
        rpcRequest.setId(id.incrementAndGet());
        rpcRequest.setClassName(method.getDeclaringClass().getName());
        rpcRequest.setMethodName(method.getName());
        rpcRequest.setParameterTypes(method.getParameterTypes());
        rpcRequest.setParameters(args);

        String[] address = serviceDiscovery.discover("dubboServer").split(":");
        String host = address[0];
        int port = Integer.parseInt(address[1]);

        if (client == null) {
            client = RpcClient.getConnect(host,port);
        }

        RpcResponse rpcResponse = client.invoke(rpcRequest);
        return rpcResponse.getResult();
    }
}
