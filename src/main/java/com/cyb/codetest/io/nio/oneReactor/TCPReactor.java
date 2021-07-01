package com.cyb.codetest.io.nio.oneReactor;

import com.cyb.codetest.io.nio.MthreadReactor;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * @author cyb
 * @date 2021/2/13 4:57 下午
 */
public class TCPReactor implements Runnable {

    private final ServerSocketChannel serverSocket;
    private final Selector selector;

    TCPReactor(int port) throws IOException {
        selector = Selector.open();
        serverSocket = ServerSocketChannel.open();
        serverSocket.socket().bind(new InetSocketAddress(port));
        //非阻塞
        serverSocket.configureBlocking(false);


        //分步处理,第一步,接收accept事件
        SelectionKey sk = serverSocket.register(selector, SelectionKey.OP_ACCEPT);
        //attach callback object, Acceptor
        //注册回调函数
        sk.attach(new Acceptor(selector, serverSocket));
    }

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {//在线程中断之前执行
                for (int i = 0; i < 2; i++) {

                    //1 查看是否有事件发生，如果没有就会一直阻塞在这里
                    //如果有事件，会将事件加入到selector的selectedKeys中
                    //这里需要操作系统内核的支持，nio的底层实现就是epoll
                    selector.select();
                    //2 如果监听到了对应的事件selectedKeys之后，遍历网络事件
                    Set selected = selector.selectedKeys();
                    Iterator it = selected.iterator();
                    while (it.hasNext()) {
                        //Reactor负责dispatch收到的事件
                        dispatch((SelectionKey) (it.next()));
                        //执行完时间之后，删除
                        it.remove();
                    }
//                    selected.clear();
                }

            }
        } catch (IOException ex) { /* ... */ }
    }


    /**
     * 执行Reactor线程的调度，执行对应的Acceptor的run方法
     *
     * @param k
     */
    void dispatch(SelectionKey k) {
        Runnable r = (Runnable) (k.attachment());
        //调用之前注册的callback对象
        if (r != null) {
            r.run();
        }
    }
}
