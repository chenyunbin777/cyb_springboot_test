package com.cyb.codetest.io.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

/**
 * @author cyb
 * @date 2021/2/13 3:59 下午
 */
public class MthreadReactor implements Runnable {

    //subReactors集合, 一个selector代表一个subReactor
    Selector[] selectors = new Selector[2];
    int next = 0;
    final ServerSocketChannel serverSocket;

    MthreadReactor(int port) throws IOException { //Reactor初始化
        selectors[0] = Selector.open();
        selectors[1] = Selector.open();
        serverSocket = ServerSocketChannel.open();
        serverSocket.socket().bind(new InetSocketAddress(port));
        //非阻塞
        serverSocket.configureBlocking(false);


        //分步处理,第一步,接收accept事件
        SelectionKey sk =
                serverSocket.register(selectors[0], SelectionKey.OP_ACCEPT);
        //attach callback object, Acceptor
        //注册回调函数
        sk.attach(new Acceptor());
    }

    /**
     * 启动Reactor服务端之后
     */
    public void run() {
        try {
            while (!Thread.interrupted()) {
                for (int i = 0; i < 2; i++) {
                    //这个地方还是阻塞的，等到事件就绪之后再返回
                    selectors[i].select();
                    //1 如果监听到了对应的事件selectedKeys之后，遍历网络事件
                    Set selected = selectors[i].selectedKeys();
                    Iterator it = selected.iterator();
                    while (it.hasNext()) {
                        //Reactor负责dispatch收到的事件
                        dispatch((SelectionKey) (it.next()));
                    }
                    selected.clear();
                }

            }
        } catch (IOException ex) { /* ... */ }
    }

    void dispatch(SelectionKey k) {
        Runnable r = (Runnable) (k.attachment());
        //调用之前注册的callback对象
        if (r != null) {
            r.run();
        }
    }


    /**
     * 处理Acceptor事件，也是一个handler,处理监听到的事件
     * 接收客户端连接并建立对应客户端的Handler，向Reactor注册此Handler。相当于NIO中建立连接的那个判断分支。
     * 比如：我监听到了SelectionKey.OP_ACCEPT事件，那么我具体调用哪个Handler来处理
     */
    class Acceptor { // ...
        public synchronized void run() throws IOException {
            SocketChannel socketChannel = serverSocket.accept(); //主selector负责accept，来建立连接
            //如果建立成功了
            if (socketChannel != null) {
                //选个subReactor去负责接收到的connection
                new AccpetorHandler(selectors[next], socketChannel);
            }
            if (++next == selectors.length) next = 0;
        }
    }

    class AccpetorHandler {
        AccpetorHandler(Selector selector, SocketChannel socketChannel) throws IOException {

            //指定非阻塞
            socketChannel.configureBlocking(false);
            //处理具体的读写数据
            SelectionKey selectionKey = socketChannel.register(selector, SelectionKey.OP_READ);

            selector.wakeup();//试一个阻塞住的selector操作立即返回
            selectionKey.attach(new READHandler(selector, socketChannel));//指定一个处理SelectionKey.OP_READ事件的handler

        }

    }

    /**
     * 处理读事件
     */
    class READHandler {

        READHandler(Selector selector, SocketChannel socketChannel) {
//            SocketChannel socketChannel = serverSocket.accept();

        }
    }
}
