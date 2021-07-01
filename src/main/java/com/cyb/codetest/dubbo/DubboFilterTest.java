//package com.cyb.codetest.dubbo;
//
//import org.apache.dubbo.common.extension.Activate;
//import org.apache.dubbo.rpc.*;
//import com.alibaba.dubbo.common.Constants;
//
///**
// * 自定义一个消费端过滤器
// * @author cyb
// * @date 2021/2/8 4:10 下午
// */
//@Activate(group = {Constants.CONSUMER, Constants.PROVIDER}, order = -30000)
//public class DubboFilterTest implements Filter {
//    @Override
//    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
//        return invoker.invoke(invocation);
//    }
//}
