package com.cyb.codetest.多线程.线程池;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author cyb
 * @date 2021/1/11 3:12 下午
 */
public class RejectExceptionHandlerTest implements RejectedExecutionHandler {
    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
        System.out.println("自定义线程池拒绝策略，实现自己的异常处理机制");
    }
}
