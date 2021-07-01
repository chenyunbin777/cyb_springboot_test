package com.cyb.codetest.io.nio.oneReactor;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * 处理服务端的事件接受监听，然后在注册一个READ事件
 *
 * @author cyb
 * @date 2021/2/13 5:00 下午
 */
public class Acceptor implements Runnable {
    private final Selector selector;
    private final ServerSocketChannel serverSocket;

    Acceptor(Selector selector, ServerSocketChannel serverSocket) {
        this.selector = selector;
        this.serverSocket = serverSocket;
    }

    @Override
    public void run() {

        try {
            //1 接受client连接请求
            SocketChannel socketChannel = serverSocket.accept();
            if (socketChannel != null) {
                //指定非阻塞
                socketChannel.configureBlocking(false);
                //处理具体的读写数据
                SelectionKey selectionKey = socketChannel.register(selector, SelectionKey.OP_READ);

                selector.wakeup();//试一个阻塞住的selector操作立即返回
                selectionKey.attach(new TCPHandler(selectionKey, socketChannel));//指定一个处理SelectionKey.OP_READ事件的handler

            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
