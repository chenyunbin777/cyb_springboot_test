package com.cyb.codetest.多线程;

import sun.net.www.http.HttpClient;

import java.io.IOException;
import java.net.URL;

/**
 * @author cyb
 * @date 2021/1/18 3:08 下午
 */
public class ThreadLocal保证httpClient线程安全 {

    static ThreadLocal threadLocal = new ThreadLocal();

    public static void main(String[] args) {
        threadLocal.set("aaa");
        threadLocal.set("bbb");
        Object o = threadLocal.get();
        System.out.println(o);
        threadLocal.remove();

    }

    //保证同一个http客户端请求
//    ThreadLocal<HttpClient> threadLocal = new ThreadLocal();

    public HttpClient getInstance() throws IOException {

        HttpClient httpClient = (HttpClient) threadLocal.get();
        if (httpClient == null) {
            threadLocal.set(HttpClient.New(new URL("")));
        }

        return httpClient;
    }

    /**
     * 当连接不用的时候要删除,删除当前线程的 ThreadLocalMap中的数据
     */
    public void release() {
        threadLocal.remove();
    }
}
