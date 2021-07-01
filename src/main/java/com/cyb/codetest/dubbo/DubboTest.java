//package com.cyb.codetest.dubbo;
//
//import com.alibaba.dubbo.config.annotation.Reference;
//import com.alibaba.dubbo.config.annotation.Service;
//import org.apache.dubbo.config.annotation.Method;
//
///**
// * @author cyb
// * @date 2021/1/6 4:01 下午
// */
//
//@Service(executes = 2,group = "DubboTest",version = "0.0.1",callbacks = 1000,loadbalance = "")
////@Method(name = "aa", executes = 10)
//public class DubboTest implements DubboTestInterface {
//
//
//    //其实服务端的超时配置是消费端的缺省配置，即如果服务端设置了超时，任务消费端可以不设置超时时间，简化了配置。
//    //客户端方法级>服务端方法级>客户端接口级>服务端接口级>客户端全局>服务端全局
//    @Reference(loadbalance = "${dubbo.consumer.loadbalance}",
//            retries = 2,
//            timeout = 3000,
//            callbacks = 1000,
//            cluster = "failsafe",
//            filter =  "这里配置过滤规则"
//
//
//    )
//    private  DubboService dubboService;
//
//
//
//    @Override
//    public void f() {
//
//    }
//}
