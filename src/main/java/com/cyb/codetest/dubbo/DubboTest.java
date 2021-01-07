package com.cyb.codetest.dubbo;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;

/**
 * @author cyb
 * @date 2021/1/6 4:01 下午
 */
@Service(executes = 2,group = "DubboTest",version = "0.0.1")
public class DubboTest implements DubboTestInterface {


    @Reference(loadbalance = "${dubbo.consumer.loadbalance}",
            retries = 2,

    )
    private  DubboService dubboService;


    @Override
    public void f() {

    }
}
