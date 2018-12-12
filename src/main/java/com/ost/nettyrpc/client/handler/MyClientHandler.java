package com.ost.nettyrpc.client.handler;

import com.ost.nettyrpc.request.RpcRequest;
import com.ost.nettyrpc.response.RpcResponse;
import com.ost.nettyrpc.response.holder.ResponseHolder;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author xyl
 * @Create 2018-11-29 10:04
 * @Desc 用于接收服务器端的返回
 **/
@Slf4j
public class MyClientHandler extends SimpleChannelInboundHandler<RpcResponse> {

    private Channel channel;

    //request Id 与 response的映射
    private Map<Long, ResponseHolder> responseMap = new ConcurrentHashMap();

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcResponse response) throws Exception {
        ResponseHolder holder = responseMap.get(response.getId());
        if (holder != null) {
            responseMap.remove(response.getId());
            holder.setResponse(response);
        }
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        super.channelRegistered(ctx);
        channel = ctx.channel();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("exceptionCaught", cause);
        ctx.close();
    }

    public RpcResponse invoke(RpcRequest rpcRequest) throws Exception {
        ResponseHolder responseHolder = new ResponseHolder();
        responseMap.put(rpcRequest.getId(),responseHolder);
        channel.writeAndFlush(rpcRequest);
        return responseHolder.getResponse();
    }
}
