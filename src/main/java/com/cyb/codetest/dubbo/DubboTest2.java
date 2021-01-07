package com.cyb.codetest.dubbo;

import com.alibaba.dubbo.config.annotation.Service;

/**
 * @author cyb
 * @date 2021/1/6 5:25 下午
 */
@Service(executes = 2,group = "DubboTest",version = "0.0.2")
public class DubboTest2 implements DubboTestInterface{


    @Override
    public void f() {

    }
}
