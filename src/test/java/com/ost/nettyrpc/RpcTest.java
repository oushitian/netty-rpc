package com.ost.nettyrpc;


import com.ost.nettyrpc.api.ISay;
import com.ost.nettyrpc.client.proxy.RpcProxy;

/**
 * @Author xyl
 * @Create 2018-11-29 15:57
 * @Desc 写点注释吧
 **/
//@Slf4j
//@RunWith(SpringRunner.class)
//@SpringBootTest
public class RpcTest {

//    @Test
//    public void test(){
//        ISay say = RpcProxy.getProxy(ISay.class);
//
//        for (int i = 0; i < 1; i++) {
//
//            String result = say.hello("luangeng" + i);
//            System.out.println(result);
//            log.info("result: " + result);

//            result = say.hello("你好，", "luangeng" + i);
//            log.info("result: " + result);
//
//            Person p = new Person();
//            p.setId(i);
//            p.setName("fd" + i);
//            p.setMan(true);
//            p.setBirth(new Date());
//            p.setList(new ArrayList<>());
//            p.getList().add("pojo" + i);
//            Person p2 = say.test(p);
//            log.info(p2.toString());
//            log.info(" ");
//        }
//    }

    public static void main(String[] args) {
        ISay say = RpcProxy.getProxy(ISay.class);
        System.out.println(say.hello("FD")+"哈哈");
    }

}
