package com.ost.rpcserver.service.impl;

import com.ost.rpcapi.api.IHello;
import com.ost.rpcserver.annotation.RpcServerAnno;
import org.springframework.stereotype.Service;

/**
 * @Author xyl
 * @Create 2018-12-18 16:54
 * @Desc 写点注释吧
 **/
@RpcServerAnno(IHello.class)
@Service
public class HelloService implements IHello {
    @Override
    public String hello(String name) {
        return "Hello! " + name;
    }
}
