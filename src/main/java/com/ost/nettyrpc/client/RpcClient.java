package com.ost.nettyrpc.client;

import com.ost.nettyrpc.client.handler.MyClientHandler;
import com.ost.nettyrpc.codec.RpcDecoder;
import com.ost.nettyrpc.codec.RpcEncoder;
import com.ost.nettyrpc.request.RpcRequest;
import com.ost.nettyrpc.response.RpcResponse;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author xyl
 * @Create 2018-11-29 9:54
 * @Desc 写点注释吧
 **/
@Slf4j
public class RpcClient {

    private Channel channel;

    private RpcClient(Channel channel){
        this.channel = channel;
    }

    //已连接主机的缓存,为了提高性能，保证有缓存的情况下不去重新连接
    private static Map<String, RpcClient> clientMap = new HashMap<String, RpcClient>();

    private static Bootstrap strap;

    static {
        EventLoopGroup workerGroup = new NioEventLoopGroup();   //客户端线程组复用，防止每次连接都新建线程组
        strap = new Bootstrap();
        strap.group(workerGroup)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
//                        pipeline.addLast(new LengthFieldBasedFrameDecoder(65535, 0, 4, 0, 4));
//                        pipeline.addLast(new ObjectDecoder(ClassResolvers.weakCachingConcurrentResolver(this.getClass().getClassLoader())));
//                        pipeline.addLast(new LengthFieldPrepender(4));
//                        pipeline.addLast(new ObjectEncoder());
                        pipeline.addLast(new RpcEncoder(RpcRequest.class)) // 将 RPC 请求进行编码（为了发送请求）
                                .addLast(new RpcDecoder(RpcResponse.class)) // 将 RPC 响应进行解码（为了处理响应）
                                .addLast(new MyClientHandler());
                    }
                });
    }

    private static RpcClient connect(String host, int port) throws InterruptedException {
        ChannelFuture future = strap.connect(host,port).sync();
        log.info("client connect to " + host + ":" + port);
        Channel c = future.channel();
        return new RpcClient(c);
    }

    public static RpcClient getConnect(String host, int port) throws InterruptedException {
        if (clientMap.containsKey(host + port)) {
            return clientMap.get(host + port);
        }
        RpcClient cilent = connect(host, port);
        clientMap.put(host + port, cilent);
        return cilent;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public RpcResponse invoke(RpcRequest rpcRequest) throws Exception {
        MyClientHandler handle = channel.pipeline().get(MyClientHandler.class);
        Assert.notNull(handle);
        return handle.invoke(rpcRequest);
    }
}
