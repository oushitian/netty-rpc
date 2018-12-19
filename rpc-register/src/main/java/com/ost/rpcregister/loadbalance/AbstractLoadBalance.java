package com.ost.rpcregister.loadbalance;

import com.ost.rpcregister.interfaces.LoadBalance;

import java.util.List;

/**
 * @Author xyl
 * @Create 2018-12-19 15:44
 * @Desc 写点注释吧
 **/
public abstract class AbstractLoadBalance implements LoadBalance {

    public String select(List<String> addressLocal){
        if (addressLocal == null || addressLocal.size() == 0)
            return null;
        if (addressLocal.size() == 1) {
            return addressLocal.get(0);
        }
        return doSelect(addressLocal);
    }

    /**
     *  模板方法，供具体的loadBalance子类去覆盖重写
     * @param addressLocal
     * @return
     */
    public abstract String doSelect(List<String> addressLocal);
}
