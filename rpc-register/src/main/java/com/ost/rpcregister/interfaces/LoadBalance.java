package com.ost.rpcregister.interfaces;

import java.util.List;

/**
 * @Author xyl
 * @Create 2018-12-19 15:43
 * @Desc 写点注释吧
 **/
public interface LoadBalance {

    String select(List<String> addressLocal);
}
