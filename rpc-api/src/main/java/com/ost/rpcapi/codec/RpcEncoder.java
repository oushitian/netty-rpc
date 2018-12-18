package com.ost.rpcapi.codec;

import com.ost.rpcapi.utils.SerializationUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @Author xyl
 * @Create 2018-12-04 11:23
 * @Desc 写点注释吧
 **/
public class RpcEncoder extends MessageToByteEncoder {
    private Class<?> genericClass;

    public RpcEncoder(Class<?> genericClass) {
        this.genericClass = genericClass;
    }

    @Override
    public void encode(ChannelHandlerContext ctx, Object in, ByteBuf out) throws Exception {
        if (genericClass.isInstance(in)) {  //in能不能被转化成genericClass
            byte[] data = SerializationUtil.serialize(in);
            out.writeInt(data.length);      //设置缓冲区的写索引
            out.writeBytes(data);           //写数据
        }
    }
}
