package com.cyb.codetest.io.nio.oneReactor;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * @author cyb
 * @date 2021/2/13 5:14 下午
 */
public class TCPHandler implements Runnable {


    private final SelectionKey selectionKey;
    private final SocketChannel socketChannel;

    public TCPHandler(SelectionKey selectionKey, SocketChannel socketChannel) {
        this.selectionKey = selectionKey;
        this.socketChannel = socketChannel;
    }

    @Override
    public void run() {
        try {
            read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void read() throws IOException {
        byte[] byteArr = new byte[1024];
        ByteBuffer byteBuffer = ByteBuffer.wrap(byteArr);


        int readInfo = socketChannel.read(byteBuffer);//读取数据
        if (readInfo == -1) {
            selectionKey.cancel();
            byteBuffer.clear();
        }

        String str = new String(byteArr);
        System.out.println("str:" + str);
        if (str != null && !str.equals("")) {
            System.out.println("处理业务逻辑");

            sendBack(str);
        }

    }

    private void sendBack(String str) throws IOException {
        String returnInfo = "我接收到了你的数据：" + str;
        ByteBuffer byteBuffer = ByteBuffer.wrap(returnInfo.getBytes());

        while (byteBuffer.hasRemaining()) {
            socketChannel.write(byteBuffer);
        }

        selectionKey.selector().wakeup();// 是一个阻塞的selector立即返回
    }
}
