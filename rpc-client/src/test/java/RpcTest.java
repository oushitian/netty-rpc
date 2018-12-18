import com.ost.rpcapi.api.IHello;
import com.ost.rpcclient.client.proxy.RpcProxy;

/**
 * @Author xyl
 * @Create 2018-11-29 15:57
 * @Desc 写点注释吧
 **/
public class RpcTest {

    public static void main(String[] args) {
        IHello say = RpcProxy.getProxy(IHello.class);
        System.out.println(say.hello("FD")+"哈哈");
    }

}
