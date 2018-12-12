package com.ost.nettyrpc.response.holder;

import com.ost.nettyrpc.response.RpcResponse;

/**
 * @Author xyl
 * @Create 2018-11-28 16:10
 * @Desc 写点注释吧
 **/
public class ResponseHolder {

    private RpcResponse response;

    public void setResponse(RpcResponse response) {
        this.response = response;
        //如果收到response就唤醒等待的线程
        synchronized (this) {
            notify();
        }
    }

    public RpcResponse getResponse() throws Exception {
        //发送完成需要等待服务器端的返回，并设置超时时间
        synchronized (this) {
            wait(10000);
        }
        return response;
    }

    public RpcResponse getResponse(long timeout) throws Exception {
        synchronized (this) {
            wait(timeout);
        }
        return response;
    }
}
