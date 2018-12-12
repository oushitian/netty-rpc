package com.ost.nettyrpc.api;

import com.ost.nettyrpc.api.pojo.Person;

/**
 * @Author xyl
 * @Create 2018-11-28 14:54
 * @Desc 写点注释吧
 **/
public interface ISay {

    String hello(String name);

    String hello(String msg, String name);

    Person test(Person person);

}
