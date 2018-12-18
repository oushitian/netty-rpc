package com.ost.rpcserver.server;


import com.ost.rpcapi.codec.RpcDecoder;
import com.ost.rpcapi.codec.RpcEncoder;
import com.ost.rpcapi.register.ServiceCenter;
import com.ost.rpcapi.request.RpcRequest;
import com.ost.rpcapi.response.RpcResponse;
import com.ost.rpcserver.server.handler.MyServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Author xyl
 * @Create 2018-11-28 16:11
 * @Desc Rpc的服务端
 **/
@Slf4j
@Component
public class RpcServer{

    private static String registerAddress;

    private static boolean isStarted = false;

    private ServiceCenter serviceCenter = new ServiceCenter();

    public void start0(String registerAddress){
        if (isStarted) {
            log.info("server has started...");
        }
        this.registerAddress = registerAddress;
        startInit();
    }

    private void startInit() {
        //启动netty的NIO创建服务器端
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap strap = new ServerBootstrap();
            strap.group(bossGroup,workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(new RpcDecoder(RpcRequest.class))
                                    .addLast(new RpcEncoder(RpcResponse.class))
                                    .addLast(new MyServerHandler());
                        }
                    });
            String[] array = registerAddress.split(":");
            String host = array[0];
            int port = Integer.parseInt(array[1]);
            ChannelFuture future = strap.bind(host, port).sync();
            //注册到zk上
            if (serviceCenter != null) {
                serviceCenter.register("dubboServer",registerAddress); // 注册服务地址
            }
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    public static void shutDown() {
        isStarted = false;
    }
}
