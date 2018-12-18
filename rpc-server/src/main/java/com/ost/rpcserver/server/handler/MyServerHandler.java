package com.ost.rpcserver.server.handler;

import com.ost.nettyrpc.request.RpcRequest;
import com.ost.nettyrpc.response.RpcResponse;
import com.ost.nettyrpc.service.ServiceInitializer;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @Author xyl
 * @Create 2018-11-28 16:21
 * @Desc 服务端的业务处理类
 **/
@Slf4j
public class MyServerHandler extends SimpleChannelInboundHandler<RpcRequest> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcRequest request) throws Exception {
        //收到信息后拼接成RpcResponse返回
        RpcResponse rpcResponse = new RpcResponse();
        log.info("server read:" + request.toString());
        rpcResponse.setId(request.getId());
        try {
            Object object = processRequest(request);
        rpcResponse.setResult(object);
        } catch (Exception e) {
            rpcResponse.setException(e);
        }
        ctx.writeAndFlush(rpcResponse);
        log.info("server send:" + rpcResponse.toString());
    }

    private Object processRequest(RpcRequest request) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        //具体解析request的请求
        String className = request.getClassName();
        String methodName = request.getMethodName();
        Class[] parameterTypes = request.getParameterTypes();
        Object[] parameters = request.getParameters();

        Object obj = ServiceInitializer.getService(className);
        Class clazz = obj.getClass();
        Method method = clazz.getMethod(methodName,parameterTypes);
        Object result = method.invoke(obj,parameters);      //      通过方法的反射调用
        return result;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.error("server caught exception", cause);
        ctx.close();
    }
}
