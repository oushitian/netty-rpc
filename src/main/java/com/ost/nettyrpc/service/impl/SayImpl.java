package com.ost.nettyrpc.service.impl;

import com.ost.nettyrpc.api.ISay;
import com.ost.nettyrpc.api.pojo.Person;
import com.ost.nettyrpc.server.anno.RpcServerAnno;
import org.springframework.stereotype.Service;

/**
 * @Author xyl
 * @Create 2018-11-29 9:51
 * @Desc api的实现类
 **/
@RpcServerAnno(ISay.class)
@Service
public class SayImpl implements ISay {
    @Override
    public String hello(String name) {
        return "Hello! " + name;
    }

    @Override
    public String hello(String msg, String name) {
        return msg + name;
    }

    @Override
    public Person test(Person person) {
        person.setId(-person.getId());
        person.setName(person.getName().toUpperCase());
        person.setMan(!person.isMan());
        person.getList().add("last");
        return person;
    }
}
